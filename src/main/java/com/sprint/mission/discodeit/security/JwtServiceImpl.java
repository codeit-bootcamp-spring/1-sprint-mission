package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

  private final UserRepository userRepository;
  private final JwtSessionRepository jwtSessionRepository;
  private final UserMapper userMapper;

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.access-expiration}")
  private long accessTokenExpiration;

  @Value("${jwt.refresh-expiration}")
  private long refreshTokenExpiration;

  private SecretKey getKey() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes());
  }

  @Override
  public JwtSession generateTokens(UserDto userDto) {
    Instant now = Instant.now();

    String accessToken = Jwts.builder()
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plusSeconds(accessTokenExpiration)))
        .claim("userDto", userDto)
        .signWith(getKey())
        .compact();

    String refreshToken = Jwts.builder()
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plusSeconds(refreshTokenExpiration)))
        .signWith(getKey())
        .compact();

    JwtSession session = JwtSession.builder()
        .userId(userDto.id())
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .issuedAt(now)
        .expiresAt(now.plusSeconds(refreshTokenExpiration))
        .build();

    return jwtSessionRepository.save(session);
  }

  @Override
  public boolean validateAccessToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }

  @Override
  public UserDto parseUserFromAccessToken(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(getKey())
        .build()
        .parseClaimsJws(token)
        .getBody();

    return claims.get("userDto", UserDto.class);
  }

  @Override
  public JwtSession rotateRefreshToken(String refreshToken) {
    JwtSession session = jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

    UserDto userDto = parseUserFromAccessToken(session.getAccessToken());
    jwtSessionRepository.delete(session);
    return generateTokens(userDto);
  }

  @Override
  public void invalidateRefreshToken(String refreshToken) {
    jwtSessionRepository.findByRefreshToken(refreshToken)
        .ifPresent(jwtSessionRepository::delete);
  }

  @Override
  public JwtSession findByRefreshToken(String refreshToken) {
    return jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new RuntimeException("Refresh token not found"));
  }
}
