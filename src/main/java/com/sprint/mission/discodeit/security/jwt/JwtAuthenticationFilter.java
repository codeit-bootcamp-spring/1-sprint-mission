package com.sprint.mission.discodeit.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
// JWT 인증을 수행하는 필터, 요청마다 실행되어 인증 상태를 SecurityContext 에 저장
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final GrantedAuthoritiesMapper authoritiesMapper;

  @Override
  // 실제 필터 로직이 실행되는 메서드, 모든 요청에 대해 실행되며 조건에 따라 인증 수행 or 건너뜀
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    // 요청에서 토큰 추출
    String token = extractTokenFromRequest(request);
    log.debug("🔍 추출된 토큰: {}", token);

    // 토큰이 있고, 유효하면 인증 처리
    if (token != null) {
      try {
        if (!jwtService.validateToken(token)) {
          response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Expired or invalid JWT token");
          return;
        }

        String username = jwtService.getUsernameFromToken(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        Collection<? extends GrantedAuthority> mappedAuthorities = authoritiesMapper.mapAuthorities(
            userDetails.getAuthorities());

        // 인증 객체 생성 후 SecurityContext에 인증 정보 저장
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, mappedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("사용자 '{}' 인증 완료", username);

      } catch (Exception e) {
        log.error("인증 처리 중 오류: {}", e.getMessage()); // 예외 시 인증 설정 없이 로깅만 수행
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
        return;
      }
    }
    filterChain.doFilter(request, response);
  }

  // 헤더에서 토큰 추출
  private String extractTokenFromRequest(HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    if (token != null && token.startsWith("Bearer ")) {
      return token.substring(7);
    }

    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if ("refreshToken".equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }

    return null;
  }

  // 인증이 필요없는 경로는 필터 건너뛰기
  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getRequestURI();
    return path.equals("/api/auth/login")
        || path.equals("/api/auth/logout")
        || path.equals("/api/auth/csrf-token")
        || path.startsWith("/swagger-ui");
  }
}
