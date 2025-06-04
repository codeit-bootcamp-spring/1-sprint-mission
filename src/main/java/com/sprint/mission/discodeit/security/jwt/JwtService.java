package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtService {

  private final JwtBlackList jwtBlackList;
  @Value("${discodeit.jwt.secret}")
  private String jwtSecretKey;

  @Value("${discodeit.jwt.access-token-expiration}")
  private long accessTokenExpiration;

  @Value("${discodeit.jwt.refresh-token-expiration}")
  private long refreshTokenExpiration;

  private final JwtSessionRepository jwtSessionRepository;
  private final ObjectMapper objectMapper;

  // AccessToken 생성 및 JWT 토큰 저장
  public String generateAccessToken(UserResponseDto userDto) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

    String token = Jwts.builder()
        .subject(userDto.username())
        .claim("userDto", objectMapper.convertValue(userDto, Map.class))
        .claim("iat", now)
        .claim("exp", expiryDate)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getSigningKey())
        .compact();

    // 기존 세션 존재 시, 제거 후 새 세션 저장
    jwtSessionRepository.findByUsername(userDto.username()).ifPresent(jwtSessionRepository::delete);

    jwtSessionRepository.save(JwtSession.builder()
        .username(userDto.username())
        .accessToken(token)
        .refreshToken("")   // refreshToken은 별도로 설정
        .issuedAt(now.toInstant())
        .expiresAt(expiryDate.toInstant())
        .build());

    return token;
  }

  // RefreshToken 생성, JWT 세션 갱신 (Rotation 전략)
  public String generateRefreshToken(String username) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + refreshTokenExpiration);

    String refreshToken = Jwts.builder()
        .subject(username)
        .claim("type", "REFRESH")
        .claim("iat", now)
        .claim("exp", expiryDate)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getSigningKey())
        .compact();

    // 기존 세션에서 username 기반으로 조회
    jwtSessionRepository.findByUsername(username).ifPresent(session -> {
      session.setRefreshToken(refreshToken);
      session.setIssuedAt(now.toInstant());
      session.setExpiresAt(expiryDate.toInstant());
      jwtSessionRepository.save(session);
    });

    return refreshToken;
  }

  // Refresh 토큰 무효화 / 세션 삭제
  public void invalidateRefreshToken(String username) {
    jwtSessionRepository.findByUsername(username).ifPresent(session -> {
      jwtSessionRepository.delete(session);
      jwtBlackList.addTokenToBlackList(session.getAccessToken(), session.getExpiresAt());
      log.info("JWT Session이 무효화되었습니다: {}", username);
    });
  }

  // 토큰 유효성 검증
  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token);

      if (jwtBlackList.isBlackListed(token)) {
        log.debug("블랙리스트에 등록된 토큰입니다: {}", token);
        return false;
      }

      return true;
    } catch (ExpiredJwtException e) {
      log.warn("토큰이 만료되었습니다: {}", e.getMessage());
      return false;
    } catch (JwtException e) {
      log.warn("유효하지 않은 토큰입니다: {}", e.getMessage());
      return false;
    }
  }

  // 토큰에서 Subject 추출
  public String getUsernameFromToken(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();

    return claims.getSubject();
  }

  // 서명용 키 생성
  private SecretKey getSigningKey() {
    byte[] keyBytes = jwtSecretKey.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

}
