package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtService {

  public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

  private final JwtSessionRepository jwtSessionRepository;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final ObjectMapper objectMapper;
  private final JwtBlacklist jwtBlacklist;

  @Value("${security.jwt.secret}")
  private String secret;

  @Value("${security.jwt.access-token-validity-seconds}")
  private int accessTokenValiditySeconds;

  @Value("${security.jwt.refresh-token-validity-seconds}")
  private int refreshTokenValiditySeconds;

  @Transactional
  public JwtSession registerJwtSession(UserDto userDto) {
    JwtObject accessToken = generateJwtToken(userDto, accessTokenValiditySeconds);
    JwtObject refreshToken = generateJwtToken(userDto, refreshTokenValiditySeconds);

    JwtSession jwtSession = JwtSession.create(userDto.id(), accessToken.token(),
        refreshToken.token(), accessToken.expirationTime());
    jwtSessionRepository.save(jwtSession);

    return jwtSession;
  }

  public boolean validate(String token) {
    boolean valid;

    try {
      JwtObject parse = parse(token);
      valid = !parse.isExpired();
      if (valid) {
        valid = !jwtBlacklist.contains(token);
      }
    } catch (DiscodeitException e) {
      log.error("error: ", e);
      valid = false;
    }

    return valid;
  }

  public JwtObject parse(String token) {
    try {
      Claims payload = Jwts.parser()
          .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
          .build()
          .parseSignedClaims(token)
          .getPayload();

      return new JwtObject(
          payload.getIssuedAt().toInstant(),
          payload.getExpiration().toInstant(),
          objectMapper.convertValue(payload.get("userDto", Map.class), UserDto.class),
          token
      );
    } catch (JwtException e) {
      log.error("error: ", e);
      throw new DiscodeitException(ErrorCode.INVALID_TOKEN, Map.of("token", token));
    }
  }

  @Transactional
  public JwtSession refreshJwtToken(String refreshToken) {
    if (!validate(refreshToken)) {
      throw new DiscodeitException(ErrorCode.INVALID_TOKEN, Map.of("refreshToken", refreshToken));
    }
    JwtSession jwtSession = jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new DiscodeitException(ErrorCode.TOKEN_NOT_FOUND, Map.of("token", refreshToken)));

    UUID userId = parse(refreshToken).userDto().id();
    UserDto userDto = userRepository.findById(userId)
        .map(userMapper::toDto)
        .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userId)));
    JwtObject accessJwtObject = generateJwtToken(userDto, accessTokenValiditySeconds);
    JwtObject refreshJwtObject = generateJwtToken(userDto, refreshTokenValiditySeconds);

    jwtSession.updateToken(
        accessJwtObject.token(),
        refreshJwtObject.token(),
        accessJwtObject.expirationTime()
    );

    return jwtSession;
  }

  @Transactional
  public void invalidateJwtSession(String refreshToken) {
    jwtSessionRepository.findByRefreshToken(refreshToken)
        .ifPresent(this::invalidate);
  }

  @Transactional
  public void invalidateJwtSession(UUID userId) {
    jwtSessionRepository.findByUserId(userId)
        .ifPresent(this::invalidate);
  }

  public JwtSession getJwtSession(String refreshToken) {
    return jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new DiscodeitException(ErrorCode.TOKEN_NOT_FOUND,
            Map.of("refreshToken", refreshToken)));
  }

  public List<JwtSession> getActiveJwtSessions() {
    return jwtSessionRepository.findAllByExpirationTimeAfter(Instant.now());
  }

  private JwtObject generateJwtToken(UserDto userDto, long tokenValiditySeconds) {
    Instant issueTime = Instant.now();
    Instant expirationTime = issueTime.plus(Duration.ofSeconds(tokenValiditySeconds));
    String token = Jwts.builder()
        .subject(userDto.username())
        .claim("userDto", userDto)
        .issuedAt(Date.from(issueTime))
        .expiration(Date.from(expirationTime))
        .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
        .compact();
    return new JwtObject(
        issueTime,
        expirationTime,
        userDto,
        token
    );
  }

  private void invalidate(JwtSession session) {
    jwtSessionRepository.delete(session);
    if (!session.isExpired()) {
      jwtBlacklist.put(session.getAccessToken(), session.getExpirationTime());
    }
  }
}
