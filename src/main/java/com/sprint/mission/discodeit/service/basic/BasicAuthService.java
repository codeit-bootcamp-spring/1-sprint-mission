package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.auth.InvalidRefreshTokenException;
import com.sprint.mission.discodeit.exception.auth.LoginFailedException;
import com.sprint.mission.discodeit.exception.auth.RefreshTokenSessionNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.security.jwt.JwtSessionRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  @Value("${discodeit.jwt.access-token-expiration}")
  private long accessTokenExpiration;

  private final AuthenticationManager authenticationManager;

  private final UserService userService;
  private final UserDetailsService userDetailsService;
  private final JwtService jwtService;

  private final JwtSessionRepository jwtSessionRepository;

  private final UserMapper userMapper;

  @Override
  public String login(LoginRequest loginRequest, HttpServletResponse response) {
    try {
      // 인증 시도
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
      );

      CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
      UserResponseDto userDto = userMapper.toResponseDto(userDetails.getUser(),
          userService.isUserOnline(userDetails.getUsername()));

      String accessToken = jwtService.generateAccessToken(userDto);
      String refreshToken = jwtService.generateRefreshToken(userDto.getUsername());

      ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
          .httpOnly(true)
          .secure(true)
          .path("/")
          .maxAge(Duration.ofDays(7))
          .sameSite("None")
          .build();

      response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

      return accessToken;

    } catch (BadCredentialsException bce) {
      throw new LoginFailedException(loginRequest.username());
    }
  }

  public void logout(String refreshToken, HttpServletResponse response) {
    if (refreshToken != null && jwtService.validateToken(refreshToken)) {
      jwtService.invalidateRefreshToken(jwtService.getUsernameFromToken(refreshToken));  // 토큰 무효화
    }

    ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(0)
        .sameSite("None")
        .build();

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }

  public String refresh(String refreshToken, HttpServletResponse response) {
    log.info("Access Token 재발급");
    if (refreshToken == null || !jwtService.validateToken(refreshToken)) {
      throw new InvalidRefreshTokenException();
    }

    String username = jwtService.getUsernameFromToken(refreshToken);
    Optional<JwtSession> sessionOptional = jwtSessionRepository.findByUsername(username);
    if (sessionOptional.isEmpty() || !refreshToken.equals(
        sessionOptional.get().getRefreshToken())) {
      throw new RefreshTokenSessionNotFoundException();
    }

    // 토큰 재발급
    CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(
        username);
    User user = customUserDetails.getUser();
    boolean isOnline = userService.isUserOnline(user.getUsername());
    String newAccessToken = jwtService.generateAccessToken(
        userMapper.toResponseDto(user, isOnline));
    String newRefreshToken = jwtService.generateRefreshToken(username);

    jwtSessionRepository.findByUsername(username).ifPresent(jwtSessionRepository::delete);

    // 세션 업데이트
    JwtSession session = JwtSession.builder()
        .username(username)
        .accessToken(newAccessToken)
        .refreshToken(newRefreshToken)
        .issuedAt(Instant.now())
        .expiresAt(Instant.now().plusMillis(accessTokenExpiration))
        .build();

    jwtSessionRepository.save(session);

    ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken)
        .httpOnly(true)
        .secure(true)
        .sameSite("None")
        .path("/")
        .maxAge(Duration.ofDays(7))
        .build();
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    return newAccessToken;
  }

}
