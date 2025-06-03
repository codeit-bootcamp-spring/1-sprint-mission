package com.sprint.mission.discodeit.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class AccessTokenFilter extends OncePerRequestFilter {
  private final JwtService jwtService;

  public AccessTokenFilter(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String header = request.getHeader("Authorization");
    if (header != null && header.startsWith("Bearer ")) {
      String jwt = header.substring(7);

      // ✅ AccessToken인지 확인
      if (jwtService.isValidAccessToken(jwt)) {
        Claims claims = jwtService.parseClaims(jwt);
        Map<String, Object> userDtoMap = claims.get("userDto", Map.class);

        String username = (String) userDtoMap.get("username");
        String role = (String) userDtoMap.get("role");

        List<GrantedAuthority> authorities =
            List.of(new SimpleGrantedAuthority("ROLE_" + role)); // 동적 권한 처리

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(username, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
      } else {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Access Token");
        return;
      }
    }

    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();
    return path.startsWith("/api/auth/login") ||
        path.startsWith("/api/auth/register") ||
        path.startsWith("/api/auth/refresh") ||
        path.startsWith("/public");
  }
}
