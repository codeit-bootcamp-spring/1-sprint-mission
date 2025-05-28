package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.data.TokenPair;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {

  private final JwtSessionRepository jwtSessionRepository;
  private final UserService userService;
  private final Key secretKey;

  private final long accessTokenValidity = 5 * 60;            // 5분
  private final long refreshTokenValidity = 60L * 60 * 24 * 21; // 3주

  public JwtServiceImpl(JwtSessionRepository jwtSessionRepository, UserService userService,
      @Value("${jwt.secret}") String secret) {
    this.jwtSessionRepository = jwtSessionRepository;
    this.userService = userService;
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
  }

  @Override
  public String generateAccessToken(UserDto userDto) {
    Instant issuedAt = Instant.now();
    Instant expiry = issuedAt.plusSeconds(accessTokenValidity);

    return Jwts.builder()
        .claim("userId", userDto.id().toString())
        .claim("username", userDto.username())
        .claim("email", userDto.email())
        .claim("role", userDto.role().name())
        .setIssuedAt(Date.from(issuedAt))
        .setExpiration(Date.from(expiry))
        .signWith(secretKey)
        .compact();
  }

  @Override
  public String generateRefreshToken() {
    Instant now = Instant.now();
    return Jwts.builder()
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plusSeconds(refreshTokenValidity)))
        .signWith(secretKey)
        .compact();
  }

  @Override
  public boolean isTokenValid(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }

  @Override
  public UserDto extractUser(String accessToken) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(accessToken)
        .getBody();

    UUID userId = UUID.fromString(claims.get("userId", String.class));
    String username = claims.get("username", String.class);

    return new UserDto(userId, username,null,null,null,null);
  }

  @Override
  public String reissueAccessToken(String refreshToken) {
    JwtSession session = jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new IllegalArgumentException("Session not found"));

    if (!isTokenValid(refreshToken)) {
      jwtSessionRepository.delete(session);
      throw new IllegalArgumentException("Invalid refresh token");
    }

    UserDto userDto = extractUser(session.getAccessToken());

    String newAccessToken = generateAccessToken(userDto);
    String newRefreshToken = generateRefreshToken();

    session.setAccessToken(newAccessToken);
    session.setRefreshToken(newRefreshToken);
    session.setIssuedAt(Instant.now());
    session.setExpiresAt(Instant.now().plusSeconds(refreshTokenValidity));

    jwtSessionRepository.save(session);

    return newAccessToken;
  }

  @Override
  public void saveSession(String refreshToken, String accessToken, UserDto userDto) {
    JwtSession session = new JwtSession();
    session.setUserId(userDto.id());
    session.setAccessToken(accessToken);
    session.setRefreshToken(refreshToken);
    session.setIssuedAt(Instant.now());
    session.setExpiresAt(Instant.now().plusSeconds(refreshTokenValidity));
    jwtSessionRepository.save(session);
  }

  @Override
  public void invalidateSession(String refreshToken) {
    jwtSessionRepository.deleteByRefreshToken(refreshToken);
  }

  @Override
  public Optional<String> findAccessTokenByRefreshToken(String refreshToken) {
    return jwtSessionRepository.findByRefreshToken(refreshToken)
        .map(JwtSession::getAccessToken);
  }

  @Override
  public TokenPair reissueTokens(String refreshToken) {
    JwtSession session = jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new JwtException("Invalid refresh token"));


    if (session.getExpiresAt().isBefore(Instant.now())) {
      jwtSessionRepository.delete(session);
      throw new JwtException("Expired refresh token");
    }


    jwtSessionRepository.delete(session);


    UserDto userDto = userService.find(session.getUserId());
    String newAccessToken = generateAccessToken(userDto);
    String newRefreshToken = UUID.randomUUID().toString();

    Instant now = Instant.now();
    Instant refreshExpiry = now.plusSeconds(refreshTokenValidity);

    JwtSession newSession = JwtSession.builder()
        .userId(userDto.id())
        .accessToken(newAccessToken)
        .refreshToken(newRefreshToken)
        .issuedAt(now)
        .expiresAt(refreshExpiry)
        .username(userDto.username())
        .role(userDto.role().name())
        .build();

    jwtSessionRepository.save(newSession);

    return new TokenPair(newAccessToken, newRefreshToken);
  }
  @Override
  public long getRefreshTokenValiditySeconds() {
    return refreshTokenValidity;
  }
  @Override
  public void invalidateAllSessionsByUserId(UUID userId) {
    jwtSessionRepository.deleteByUserId(userId);
  }

}