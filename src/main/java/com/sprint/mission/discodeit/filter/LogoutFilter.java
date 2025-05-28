package com.sprint.mission.discodeit.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class LogoutFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {

    request.getSession(false); // 세션이 없으면 null 반환
    if (request.getSession(false) != null) {
      request.getSession().invalidate();
    }

    SecurityContextHolder.clearContext();

    response.setStatus(HttpServletResponse.SC_OK);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return !request.getRequestURI().equals("/api/auth/logout") ||
        !request.getMethod().equalsIgnoreCase("POST");
  }
}
