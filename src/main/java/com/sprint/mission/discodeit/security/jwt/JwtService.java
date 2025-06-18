package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.text.ParseException;
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
public class JwtService {

  public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

  @Value("${security.jwt.secret}")
  private String secret;
  @Value("${security.jwt.access-token-validity-seconds}")
  private long accessTokenValiditySeconds;
  @Value("${security.jwt.refresh-token-validity-seconds}")
  private long refreshTokenValiditySeconds;

  private final JwtSessionRepository jwtSessionRepository;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final ObjectMapper objectMapper;
  private final JwtBlacklist jwtBlacklist;

  @Transactional
  public JwtSession registerJwtSession(UserDto userDto) {
    JwtObject accessJwtObject = generateJwtObject(userDto, accessTokenValiditySeconds);
    JwtObject refreshJwtObject = generateJwtObject(userDto, refreshTokenValiditySeconds);

    JwtSession jwtSession = new JwtSession(userDto.id(), accessJwtObject.token(),
        refreshJwtObject.token(), accessJwtObject.expirationTime());
    jwtSessionRepository.save(jwtSession);

    return jwtSession;
  }

  public boolean validate(String token) {
    boolean verified;

    try {
      JWSVerifier verifier = new MACVerifier(secret);
      JWSObject jwsObject = JWSObject.parse(token);
      verified = jwsObject.verify(verifier);

      if (verified) {
        JwtObject jwtObject = parse(token);
        verified = !jwtObject.isExpired();
      }

      if (verified) {
        verified = !jwtBlacklist.contains(token);
      }

    } catch (JOSEException | ParseException e) {
      log.error(e.getMessage());
      verified = false;
    }

    return verified;
  }

  public JwtObject parse(String token) {
    try {
      JWSObject jwsObject = JWSObject.parse(token);
      Payload payload = jwsObject.getPayload();
      Map<String, Object> jsonObject = payload.toJSONObject();
      return new JwtObject(
          objectMapper.convertValue(jsonObject.get("iat"), Instant.class),
          objectMapper.convertValue(jsonObject.get("exp"), Instant.class),
          objectMapper.convertValue(jsonObject.get("userDto"), UserDto.class),
          token
      );
    } catch (ParseException e) {
      log.error(e.getMessage());
      throw new DiscodeitException(ErrorCode.INVALID_TOKEN, Map.of("token", token), e);
    }

  }

  @Transactional
  public JwtSession refreshJwtSession(String refreshToken) {
    if (!validate(refreshToken)) {
      throw new DiscodeitException(ErrorCode.INVALID_TOKEN, Map.of("refreshToken", refreshToken));
    }
    JwtSession session = jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new DiscodeitException(ErrorCode.TOKEN_NOT_FOUND,
            Map.of("refreshToken", refreshToken)));

    UUID userId = parse(refreshToken).userDto().id();
    UserDto userDto = userRepository.findById(userId)
        .map(userMapper::toDto)
        .orElseThrow(() -> UserNotFoundException.withId(userId));
    JwtObject accessJwtObject = generateJwtObject(userDto, accessTokenValiditySeconds);
    JwtObject refreshJwtObject = generateJwtObject(userDto, refreshTokenValiditySeconds);

    session.update(
        accessJwtObject.token(),
        refreshJwtObject.token(),
        accessJwtObject.expirationTime()
    );

    return session;
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

  private JwtObject generateJwtObject(UserDto userDto, long tokenValiditySeconds) {
    Instant issueTime = Instant.now();
    Instant expirationTime = issueTime.plus(Duration.ofSeconds(tokenValiditySeconds));

    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        .subject(userDto.username())
        .claim("userDto", userDto)
        .issueTime(new Date(issueTime.toEpochMilli()))
        .expirationTime(new Date(expirationTime.toEpochMilli()))
        .build();

    JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
    SignedJWT signedJWT = new SignedJWT(header, claimsSet);

    try {
      signedJWT.sign(new MACSigner(secret));
    } catch (JOSEException e) {
      log.error(e.getMessage());
      throw new DiscodeitException(ErrorCode.INVALID_TOKEN_SECRET, e);
    }

    String token = signedJWT.serialize();

    return new JwtObject(issueTime, expirationTime, userDto, token);
  }

  private void invalidate(JwtSession session) {
    jwtSessionRepository.delete(session);
    if (!session.isExpired()) {
      jwtBlacklist.put(session.getAccessToken(), session.getExpirationTime());
    }
  }
}
