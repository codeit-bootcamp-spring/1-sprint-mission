package com.sprint.mission.discodeit.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class LogoutFilter extends OncePerRequestFilter {

  private static final String LOGOUT_URI = "/api/auth/logout";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    if (!request.getMethod().equalsIgnoreCase("POST") || !request.getRequestURI().equals(LOGOUT_URI)) {
      filterChain.doFilter(request, response);
      return;
    }

    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }

    SecurityContextHolder.clearContext();

    response.setStatus(HttpServletResponse.SC_OK);

  }
}
