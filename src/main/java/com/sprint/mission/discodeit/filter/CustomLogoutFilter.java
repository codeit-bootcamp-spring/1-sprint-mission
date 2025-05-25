package com.sprint.mission.discodeit.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

// 로그아웃 필터 구현
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutFilter extends OncePerRequestFilter {

  private final SessionRegistry sessionRegistry;
  private final PersistentTokenRepository persistentTokenRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

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

      // 5. 쿠키 삭제 (JSESSIONID, Remember-Me, CSRF 토큰)
      deleteCookie(response, "JSESSIONID");
      deleteCookie(response, "remember-me"); // Remember-Me 쿠키 삭제
      deleteCookie(response, "CSRF-TOKEN");

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
