package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

  private final UserRepository userRepository;
  private final JwtSessionRepository jwtSessionRepository;
  private final ObjectMapper objectMapper;
  private final UserMapper userMapper;

  @Value("${jwt.secret:defaultSecretKeyForDevelopmentEnvironmentOnly}")
  private String secretKey;

  @Value("${jwt.access-token.expiration:3600}")
  private long accessTokenExpiration;

  @Value("${jwt.refresh-token.expiration:604800}")
  private long refreshTokenExpiration;

  private Key key;

  @PostConstruct
  public void init() {
    this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  @Transactional
  public JwtTokens generateTokens(UserDto userDto) {
    User user = userRepository.findById(userDto.id())
        .orElseThrow(() -> UserNotFoundException.withId(userDto.id()));

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime accessTokenExpiresAt = now.plusSeconds(accessTokenExpiration);
    LocalDateTime refreshTokenExpiresAt = now.plusSeconds(refreshTokenExpiration);

    String accessToken = createToken(userDto, now, accessTokenExpiresAt);
    String refreshToken = createRefreshToken(userDto.id(), now, refreshTokenExpiresAt);

    JwtSession jwtSession = JwtSession.builder()
        .user(user)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .createdAt(now)
        .accessTokenExpiresAt(accessTokenExpiresAt)
        .refreshTokenExpiresAt(refreshTokenExpiresAt)
        .isValid(true)
        .build();

    jwtSessionRepository.save(jwtSession);

    return new JwtTokens(accessToken, refreshToken);
  }

  public Optional<UserDto> validateToken(String token) {
    try {
      Claims claims = Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token)
          .getBody();

      String userDtoJson = claims.get("userDto", String.class);
      UserDto userDto = objectMapper.readValue(userDtoJson, UserDto.class);

      Optional<JwtSession> session = jwtSessionRepository.findByAccessTokenAndIsValidTrue(token);
      if (session.isEmpty() || session.get().isAccessTokenExpired()) {
        return Optional.empty();
      }

      return Optional.of(userDto);
    } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException |
             SignatureException | IllegalArgumentException | JsonProcessingException e) {
      log.error("JWT 검증 오류: {}", e.getMessage());
      return Optional.empty();
    }
  }

  @Transactional
  public Optional<JwtTokens> refreshToken(String refreshToken) {
    try {
      Claims claims = Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(refreshToken)
          .getBody();

      UUID userId = UUID.fromString(claims.getSubject());

      Optional<JwtSession> sessionOpt = jwtSessionRepository.findByRefreshTokenAndIsValidTrue(
          refreshToken);
      if (sessionOpt.isEmpty() || sessionOpt.get().isRefreshTokenExpired()) {
        return Optional.empty();
      }

      JwtSession session = sessionOpt.get();
      User user = session.getUser();
      UserDto userDto = userMapper.toDto(user);

      LocalDateTime now = LocalDateTime.now();
      LocalDateTime accessTokenExpiresAt = now.plusSeconds(accessTokenExpiration);
      LocalDateTime refreshTokenExpiresAt = now.plusSeconds(refreshTokenExpiration);

      String newAccessToken = createToken(userDto, now, accessTokenExpiresAt);
      String newRefreshToken = createRefreshToken(userId, now, refreshTokenExpiresAt);

      session.updateTokens(newAccessToken, newRefreshToken, accessTokenExpiresAt,
          refreshTokenExpiresAt);
      jwtSessionRepository.save(session);

      return Optional.of(new JwtTokens(newAccessToken, newRefreshToken));
    } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException |
             SignatureException | IllegalArgumentException e) {
      log.error("Refresh token 검증 오류: {}", e.getMessage());
      return Optional.empty();
    }
  }

  @Transactional
  public void invalidateToken(String refreshToken) {
    Optional<JwtSession> session = jwtSessionRepository.findByRefreshTokenAndIsValidTrue(
        refreshToken);
    session.ifPresent(jwtSession -> {
      jwtSession.invalidate();
      jwtSessionRepository.save(jwtSession);
    });
  }

  @Transactional
  public void invalidateAllUserSessions(UUID userId) {
    jwtSessionRepository.invalidateAllUserSessions(userId);
  }

  private String createToken(UserDto userDto, LocalDateTime issuedAt, LocalDateTime expiresAt) {
    try {
      Date issuedAtDate = Date.from(issuedAt.atZone(ZoneId.systemDefault()).toInstant());
      Date expirationDate = Date.from(expiresAt.atZone(ZoneId.systemDefault()).toInstant());

      return Jwts.builder()
          .setSubject(userDto.id().toString())
          .claim("userDto", objectMapper.writeValueAsString(userDto))
          .setIssuedAt(issuedAtDate)
          .setExpiration(expirationDate)
          .signWith(key, SignatureAlgorithm.HS256)
          .compact();
    } catch (JsonProcessingException e) {
      throw new RuntimeException("직렬화 실패", e);
    }
  }

  private String createRefreshToken(UUID userId, LocalDateTime issuedAt, LocalDateTime expiresAt) {
    Date issuedAtDate = Date.from(issuedAt.atZone(ZoneId.systemDefault()).toInstant());
    Date expirationDate = Date.from(expiresAt.atZone(ZoneId.systemDefault()).toInstant());

    return Jwts.builder()
        .setSubject(userId.toString())
        .setIssuedAt(issuedAtDate)
        .setExpiration(expirationDate)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public Optional<String> getAccessTokenByRefreshToken(String refreshToken) {
    Optional<JwtSession> session = jwtSessionRepository.findByRefreshTokenAndIsValidTrue(
        refreshToken);
    return session.map(JwtSession::getAccessToken);
  }

  public record JwtTokens(String accessToken, String refreshToken) {

  }
}
