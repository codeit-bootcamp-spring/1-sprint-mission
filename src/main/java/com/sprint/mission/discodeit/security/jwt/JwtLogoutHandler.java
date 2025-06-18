package com.sprint.mission.discodeit.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

  private final JwtService jwtService;

  @SneakyThrows
  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    resolveRefreshToken(request)
        .ifPresent(refreshToken -> {
          jwtService.invalidateJwtSession(refreshToken);
          invalidateRefreshTokenCookie(response);
        });
  }

  private Optional<String> resolveRefreshToken(HttpServletRequest request) {
    return Arrays.stream(request.getCookies())
        .filter(cookie -> cookie.getName().equals(JwtService.REFRESH_TOKEN_COOKIE_NAME))
        .findFirst()
        .map(Cookie::getValue);
  }

  private void invalidateRefreshTokenCookie(HttpServletResponse response) {
    Cookie refreshTokenCookie = new Cookie(JwtService.REFRESH_TOKEN_COOKIE_NAME, "");
    refreshTokenCookie.setMaxAge(0);
    refreshTokenCookie.setHttpOnly(true);
    response.addCookie(refreshTokenCookie);
  }
}