package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.JwtSession;
import com.sprint.mission.discodeit.repository.JwtSessionRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

  private final JwtSessionRepository jwtSessionRepository;

  @Value("${jwt.secret}")
  private String secret;

  private Key key;

  @PostConstruct
  public void init() {
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
  }

  public String generateAccessToken(UserDto userDto) {
    return Jwts.builder()
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15)) // 15 min
        .claim("userDto", userDto)
        .signWith(key)
        .compact();
  }

  public String generateRefreshToken() {
    return UUID.randomUUID().toString();
  }

  public String generateTokens(UserDto userDto) {
    String accessToken = generateAccessToken(userDto);
    String refreshToken = generateRefreshToken();

    JwtSession session = new JwtSession(
        userDto.id(), accessToken, refreshToken,
        LocalDateTime.now(), LocalDateTime.now().plusMinutes(15)
    );
    jwtSessionRepository.save(session);

    return accessToken;
  }

  public boolean isValid(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }

  public String rotateRefreshToken(String oldRefreshToken, UserDto userDto) {
    Optional<JwtSession> sessionOpt = jwtSessionRepository.findByRefreshToken(oldRefreshToken);
    if (sessionOpt.isEmpty()) {
      throw new IllegalArgumentException("Refresh token invalid");
    }
    JwtSession session = sessionOpt.get();
    String newAccessToken = generateAccessToken(userDto);
    String newRefreshToken = generateRefreshToken();

    session.setAccessToken(newAccessToken);
    session.setRefreshToken(newRefreshToken);
    session.setIssuedAt(LocalDateTime.now());
    session.setExpiresAt(LocalDateTime.now().plusMinutes(15));

    jwtSessionRepository.save(session);
    return newAccessToken;
  }

  public String getAccessTokenByRefresh(String refreshToken) {
    return jwtSessionRepository.findByRefreshToken(refreshToken)
        .map(JwtSession::getAccessToken)
        .orElseThrow(() -> new IllegalArgumentException("No session found"));
  }

  public void invalidate(String refreshToken) {
    jwtSessionRepository.deleteByRefreshToken(refreshToken);
  }
}
