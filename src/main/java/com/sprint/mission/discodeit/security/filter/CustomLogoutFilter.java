package com.sprint.mission.discodeit.security.filter;

import com.sprint.mission.discodeit.security.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.filter.GenericFilterBean;

// 로그아웃 필터 구현
@Slf4j
@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

  private final SessionRegistry sessionRegistry;
  private final PersistentTokenRepository persistentTokenRepository;
  private final JwtService jwtService;

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    if ("/api/auth/logout".equals(request.getRequestURI()) &&
        "POST".equalsIgnoreCase(request.getMethod())) {

      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication != null && authentication.isAuthenticated()) {

        String username = authentication.getName();
        Object principal = authentication.getPrincipal();

        // 1. Remember-Me 토큰 삭제 (데이터베이스)
        try {
          persistentTokenRepository.removeUserTokens(username);
          log.info("Remember-Me tokens removed for user: {}", username);
        } catch (Exception e) {
          log.error("Failed to remove remember-me tokens for user: {}", username, e);
        }

        // 해당 사용자의 모든 세션 정보 가져오기
        List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
        // 모든 세션 만료 및 제거
        for (SessionInformation sessionInfo : sessions) {
          sessionInfo.expireNow();
          sessionRegistry.removeSessionInformation(sessionInfo.getSessionId());
        }
      }

      // SecurityContext 초기화
      SecurityContextHolder.clearContext();
      // 세션 무효화
      HttpSession session = request.getSession(false);
      if (session != null) {
        session.invalidate();
      }

      // 5. 토큰 무효화
      Cookie refreshTokenCookie = Arrays.stream(request.getCookies())
          .filter(cookie -> cookie.getName().equals("refresh-token"))
          .findFirst()
          .orElseThrow(() -> new ServletException("Cookie not found"));

      jwtService.invalidateRefreshToken(refreshTokenCookie.getValue());

      // 6. 쿠키 삭제 (JSESSIONID, Remember-Me, CSRF 토큰)
      deleteCookie(response, "JSESSIONID");
      deleteCookie(response, "remember-me"); // Remember-Me 쿠키 삭제
      deleteCookie(response, "CSRF-TOKEN");
      deleteCookie(response, "refresh-token");

      response.setStatus(HttpServletResponse.SC_OK); //성공 반환
      response.setContentType("application/json");
      response.getWriter().write("{\"status\":\"success\",\"message\":\"로그아웃 완료.\"}");

      return;
    }

    filterChain.doFilter(request, response);
  }

  private void deleteCookie(HttpServletResponse response, String cookieName) {
    ResponseCookie deleteCookie = ResponseCookie.from(cookieName, "")
        .path("/")
        .maxAge(0)
        .secure(false) // 운영에서는 true
        .httpOnly(true)
        .sameSite("Lax")
        .build();

    response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
    log.debug("Cookie 삭제: {}", cookieName);

  }
}
