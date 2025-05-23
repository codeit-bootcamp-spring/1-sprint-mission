package com.sprint.mission.discodeit.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

// 로그아웃 필터 구현
@Component
@RequiredArgsConstructor
public class CustomLogoutFilter extends OncePerRequestFilter {

  private final SessionRegistry sessionRegistry;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    if ("/api/auth/logout".equals(request.getRequestURI()) &&
        "POST".equalsIgnoreCase(request.getMethod())) {

      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication != null && authentication.isAuthenticated()) {
        Object principal = authentication.getPrincipal();

        // 해당 사용자의 모든 세션 정보 가져오기
        List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);

        // 모든 세션 만료 및 제거
        for (SessionInformation sessionInfo : sessions) {
          sessionInfo.expireNow();
          sessionRegistry.removeSessionInformation(sessionInfo.getSessionId());
        }
      }

      // 로그아웃
      SecurityContextHolder.clearContext(); //SecurityContext 초기화
      HttpSession session = request.getSession(false);
      if (session != null) {
        session.invalidate();
      }

      // 쿠키 삭제
      ResponseCookie delete = ResponseCookie.from("JSESSIONID", "")
          .path("/")
          .maxAge(0) //유효시간 만료 처리
          .secure(true)
          .httpOnly(true)
          .build();
      response.addHeader(HttpHeaders.SET_COOKIE, delete.toString());

      //CSRF 토큰 삭제
      ResponseCookie csrf = ResponseCookie.from("CSRF-TOKEN", "")
          .path("/")
          .maxAge(0)
          .secure(true)
          .httpOnly(true)
          .build();
      response.addHeader(HttpHeaders.SET_COOKIE, csrf.toString());

      response.setStatus(HttpServletResponse.SC_OK); //성공 반환
      return;
    }

    filterChain.doFilter(request, response);
  }
}
