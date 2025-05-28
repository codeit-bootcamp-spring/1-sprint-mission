package com.sprint.mission.discodeit.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class DiscodeitLogoutFilter extends OncePerRequestFilter {

  private final List<String> cookieNames = List.of("JSESSIONID");

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    if (!(request.getServletPath().equals("/api/auth/logout") && HttpMethod.POST.matches(request.getMethod()))) {
      filterChain.doFilter(request, response);
      return;
    }

    SecurityContextHolder.clearContext();

    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }

    for (String name : cookieNames) {
      ResponseCookie expired = ResponseCookie.from(name, "")
          .path("/")
          .maxAge(0)
          .httpOnly(true)
          .build();
      response.addHeader(HttpHeaders.SET_COOKIE, expired.toString());
    }
    response.setStatus(HttpStatus.OK.value());
  }
}
