package com.sprint.mission.discodeit.service.basic;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.JwtSession;
import com.sprint.mission.discodeit.repository.JwtSessionRepository;
import com.sprint.mission.discodeit.dto.UserDto;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

  @Value("${jwt.secret_key}")
  private String secret;

  private final JwtSessionRepository jwtSessionRepository;

  public String generateAccessToken(UserDto userDto) {
    Instant now = Instant.now();
    Instant exp = now.plusSeconds(3600);

    return JWT.create()
        .withIssuedAt(Date.from(now))
        .withExpiresAt(Date.from(exp))
        .withClaim("userDto", userDto.toString())
        .sign(Algorithm.HMAC256(secret));
  }

  public String generateRefreshToken(UserDto userDto) {
    return JWT.create()
        .withIssuedAt(new Date())
        .withExpiresAt(Date.from(Instant.now().plusSeconds(604800)))
        .withClaim("userDto", userDto.toString())
        .sign(Algorithm.HMAC256(secret));
  }

  public void saveJwtSession(UserDto userDto, String accessToken, String refreshToken) {
    JwtSession session = JwtSession.builder()
        .userId(userDto.id())
        .userDto(userDto)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
    jwtSessionRepository.save(session);
  }

  public void invalidateRefreshToken(String refreshToken) {
    jwtSessionRepository.findByRefreshToken(refreshToken).ifPresent(jwtSessionRepository::delete);
  }

  private Algorithm getAlgorithm() {
    return Algorithm.HMAC256(secret);
  }

  public boolean isAccessTokenValid(String token) {
    try {
      JWTVerifier verifier = JWT.require(getAlgorithm()).build();
      verifier.verify(token);
      return true;
    } catch (JWTVerificationException e) {
      return false;
    }
  }

  public UserDetails extractUserDetails(String token) {
    try {
      JWTVerifier verifier = JWT.require(getAlgorithm()).build();
      DecodedJWT decodedJWT = verifier.verify(token);

      String user = decodedJWT.getClaim("userDetails").asString();

      ObjectMapper objectMapper = new ObjectMapper();

      return objectMapper.readValue(user, UserDetails.class);

    } catch (Exception e) {
      throw new RuntimeException("Invalid token or cannot extract user info", e);
    }
  }

  public Optional<JwtSession> findJwtSessionByRefreshToken(String refreshToken) {
    return jwtSessionRepository.findByRefreshToken(refreshToken);
  }

  public boolean isRefreshTokenValid(String refreshToken) {
    try {
      JWTVerifier verifier = JWT.require(getAlgorithm()).build();
      verifier.verify(refreshToken);
      return false;
    } catch (JWTVerificationException e) {
      return true;
    }
  }

  public Optional<String> reissueAccessTokenWithRotation(String oldRefreshToken,
      HttpServletResponse response) {
    if (isRefreshTokenValid(oldRefreshToken)) {
      return Optional.empty();
    }

    Optional<JwtSession> sessionOpt = findJwtSessionByRefreshToken(oldRefreshToken);
    if (sessionOpt.isEmpty()) {
      return Optional.empty();
    }

    JwtSession session = sessionOpt.get();

    UserDto userDto = extractUserDtoFromToken(oldRefreshToken);
    if (userDto == null) {
      return Optional.empty();
    }

    String newAccessToken = generateAccessToken(userDto);
    String newRefreshToken = generateRefreshToken(userDto);

    session.setAccessToken(newAccessToken);
    session.setRefreshToken(newRefreshToken);
    jwtSessionRepository.save(session);

    ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken)
        .httpOnly(true)
        .path("/")
        .maxAge(Duration.ofDays(7))
        .build();
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    return Optional.of(newAccessToken);
  }

  public UserDto extractUserDtoFromToken(String token) {
    try {
      DecodedJWT decodedJWT = JWT.decode(token);
      String userJson = decodedJWT.getClaim("userDto").asString();
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(userJson, UserDto.class);
    } catch (Exception e) {
      return null;
    }
  }

  public void invalidateSessionsByUserId(UUID id) {
    List<JwtSession> sessions = jwtSessionRepository.findAllByUserId(id);
    jwtSessionRepository.deleteAll(sessions);
  }
}
