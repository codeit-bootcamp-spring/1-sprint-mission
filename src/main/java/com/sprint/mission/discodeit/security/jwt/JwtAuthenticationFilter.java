package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.UserDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;


@RequiredArgsConstructor
@Slf4j
/**
 * JWT 인증 수행 필터 요청마다 실행되어 인증 상태를 SecurityContext 에 저장
 **/
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    // 요청에서 토큰 추출
    String token = extractTokenFromRequest(request);

    // 토큰이 있고, 유효하면 인증 처리
    if (token != null && jwtService.validateToken(token)) {
      try {
        String username = jwtService.getUserDtoFromToken(token)
            .map(UserDto::getUsername)
            .orElse(null);
        if (username == null) {
          throw new AuthenticationException("유효하지 않은 토큰입니다. : username이 없습니다.") { // 401
          };
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        log.debug("사용자 {} 인증 완료", username);
      } catch (Exception e) {
        log.error("인증 처리 중 오류 : {}", e.getMessage());
      }
    }
    filterChain.doFilter(request, response);
  }

  private String extractTokenFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    String method = request.getMethod();

    return path.equals("/") ||
        path.equals("/index.html") ||
        path.startsWith("/assets/") ||
        path.equals("/favicon.ico") ||
        path.startsWith("/h2-console/") ||
        path.equals("/api/auth/login") ||
        path.startsWith("/swagger-ui/") ||
        path.startsWith("v3/api-docs/") ||
        path.startsWith("/actuator/") ||
        (path.equals("/api/users") && "POST".equals(method));
  }
}
