package com.sprint.mission.discodeit.filter;

import com.sprint.mission.discodeit.security.jwt.JwtHeader;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (header != null && header.startsWith(JwtHeader.JWT_BEARER)) {

      String token = header.substring(7);
      if (jwtService.validate(token)) {
        Authentication authentication = jwtService.createAuthentication(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } else {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    filterChain.doFilter(request, response);
  }


  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();
    if (!path.startsWith("/api/")) {
      return true;
    }

    if (path.startsWith("/api/auth/login") ||
        path.startsWith("/api/auth/csrf-token") ||
        path.startsWith("/api/auth/me") ||
        path.startsWith("/api/auth/logout")
    ) {
      return true;
    }
    return false;
  }
}
