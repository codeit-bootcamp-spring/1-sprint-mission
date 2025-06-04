package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProvider {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.access-token.validity-seconds}")
  private long accessTokenExpiration;

  @Value("${jwt.refresh-token.validity-seconds}")
  private long refreshTokenExpiration;

  public String generateAccessToken(UserDto userDto) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

    return Jwts.builder()
        .claim("userDto", userDto)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getSigningKey())
        .compact();
  }

  public String generateRefreshToken(UserDto userDto) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + refreshTokenExpiration);

    return Jwts.builder()
        .claim("userDto", userDto)
        .claim("type", "REFRESH")
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getSigningKey())
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (ExpiredJwtException e) {
      log.warn("토큰이 만료되었습니다: {}", e.getMessage());
      return false;
    } catch (JwtException e) {
      log.warn("유효하지 않은 토큰입니다: {}", e.getMessage());
      return false;
    }
  }

  public UserDto getUserDtoFromToken(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();

    Map<String, Object> userMap = claims.get("userDto", Map.class);
    return objectMapper.convertValue(userMap, UserDto.class);
  }

  private SecretKey getSigningKey() {
    byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
