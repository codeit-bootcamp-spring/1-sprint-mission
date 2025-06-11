package com.sprint.mission.discodeit.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Slf4j
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

  private final JwtService jwtService;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {

    if ("/api/auth/logout".equals(request.getRequestURI()) &&
        "POST".equalsIgnoreCase(request.getMethod())) {

      // 토큰 무효화
      Cookie refreshTokenCookie = null;
      try {
        refreshTokenCookie = Arrays.stream(request.getCookies())
            .filter(cookie -> cookie.getName().equals(JwtService.REFRESH_TOKEN_COOKIE_NAME))
            .findFirst()
            .orElseThrow(() -> new ServletException("Cookie not found"));
      } catch (ServletException e) {
        throw new RuntimeException(e);
      }

      jwtService.invalidateRefreshToken(refreshTokenCookie.getValue());

      ResponseCookie deleteCookie = ResponseCookie.from(JwtService.REFRESH_TOKEN_COOKIE_NAME, "")
          .path("/") //질문 - path가 "/"인 경우와 "/api/auth"인 경우의 쿠키 동작차이점
          .maxAge(0)
          .secure(false) // 운영에서는 true
          .httpOnly(true)
          .sameSite("Lax")
          .build();

      response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
      log.debug("Cookie 삭제: {}", JwtService.REFRESH_TOKEN_COOKIE_NAME);

      response.setStatus(HttpServletResponse.SC_OK); //성공 반환
      response.setContentType("application/json");
    }
  }

}
