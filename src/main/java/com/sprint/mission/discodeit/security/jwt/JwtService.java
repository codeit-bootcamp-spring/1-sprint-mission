package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.AuthException;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.JwtSessionRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.UserOnlineStatusService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {


  @Value("${app.jwt.secret}")
  private String jwtSecret;

  @Value("${app.jwt.access-token-expiration}")
  private long accessTokenExpiration;

  @Value("${app.jwt.refresh-token-expiration}")
  private long refreshTokenExpiration;
  private final ObjectMapper objectMapper = new ObjectMapper();

  private final UserRepository userRepository;
  private final JwtSessionRepository jwtSessionRepository;
  private final UserMapper userMapper;
  private final UserOnlineStatusService userOnlineStatusService;

  public JwtSession generateJwtSession(UserDto userDto) {

    jwtSessionRepository.findByUser_Id(userDto.id())
        .ifPresent(session -> {
          Instant expirationTime = extractExpiry(session.getAccessToken());
          JwtBlacklist.addToBlacklist(session.getAccessToken(), expirationTime);

          jwtSessionRepository.delete(session);
        });

    String accessToken = generateAccessToken(userDto);
    String refreshToken = generateRefreshToken(userDto.id());

    User user = userRepository.findById(userDto.id())
        .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

    JwtSession session = new JwtSession(user, accessToken, refreshToken);

    return jwtSessionRepository.save(session);
  }

  public boolean validateToken(String token) {
    try {

      if (JwtBlacklist.isBlacklisted(token)) {
        log.warn("블랙리스트에 등록된 토큰입니다. TOKEN : {}", token);
        return false;
      }

      Jwts.parserBuilder()
          .setSigningKey(getSigningKey())
          .build()
          .parseClaimsJws(token);

      return true;
    } catch (ExpiredJwtException e) {
      log.warn("토큰이 만료되었습니다: {}", e.getMessage());
      return false;
    } catch (JwtException e) {
      log.warn("유효하지 않은 토큰입니다: {}", e.getMessage());
      return false;
    }
  }

  public JwtSession rotateRefreshToken(String oldRefreshToken) {

    JwtSession session = jwtSessionRepository.findByRefreshToken(oldRefreshToken)
        .orElseThrow(() -> new AuthException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

    if (!validateToken(oldRefreshToken)) {
      jwtSessionRepository.delete(session);
      throw new DiscodeitException(ErrorCode.INVALID_TOKEN);
    }

    User user = session.getUser();
    UserDto dto = userMapper.toDto(user, userOnlineStatusService);

    String newAccessToken = generateAccessToken(dto);
    String newRefreshToken = generateRefreshToken(dto.id());

    session.updateTokens(newAccessToken, newRefreshToken);

    return jwtSessionRepository.save(session);
  }

  public void invalidateRefreshToken(String refreshToken) {

    JwtSession session = jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new AuthException(ErrorCode.INVALID_TOKEN));

    JwtBlacklist.addToBlacklist(session.getAccessToken(), extractExpiry(session.getAccessToken()));

    jwtSessionRepository.delete(session);
  }

  public void invalidateUserSession(UUID userId) {

    JwtSession session = jwtSessionRepository.findByUser_Id(userId)
        .orElseThrow(() -> new AuthException(ErrorCode.INVALID_TOKEN));

    JwtBlacklist.addToBlacklist(session.getAccessToken(), extractExpiry(session.getAccessToken()));

    jwtSessionRepository.delete(session);
  }

  public String extractUsername(String accessToken) {
    try {
      Claims claims = Jwts.parserBuilder()
          .setSigningKey(getSigningKey())
          .build()
          .parseClaimsJws(accessToken)
          .getBody();

      UserDto dto = objectMapper.convertValue(claims.get("userDto"), UserDto.class);
      return dto.username();
    } catch (Exception e) {
      throw new DiscodeitException(ErrorCode.INVALID_TOKEN);
    }
  }

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes());
  }

  private String generateAccessToken(UserDto dto) {
    Instant now = Instant.now();
    Instant expirationTime = now.plusMillis(accessTokenExpiration);

    try {
      return Jwts.builder()
          .setSubject(dto.id().toString())
          .claim("userDto", dto)
          .setIssuedAt(Date.from(now))
          .setExpiration(Date.from(expirationTime))
          .signWith(getSigningKey())
          .compact();
    } catch (Exception e) {
      throw new DiscodeitException(ErrorCode.ACCESS_DENIED); // TODO : 상세화
    }
  }

  private String generateRefreshToken(UUID userId) {
    Instant now = Instant.now();
    Instant expirationTime = now.plusMillis(refreshTokenExpiration);

    return Jwts.builder()
        .claim("userId", userId.toString())
        .claim("type", "REFRESH")
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(expirationTime))
        .signWith(getSigningKey())
        .compact();
  }

  public Instant extractExpiry(String token) {
    try {
      Claims claims = Jwts.parserBuilder()
          .setSigningKey(getSigningKey())
          .build()
          .parseClaimsJws(token)
          .getBody();

      Date expiration = claims.getExpiration();
      return expiration.toInstant();
    } catch (Exception e) {
      return Instant.EPOCH;
    }
  }
}
