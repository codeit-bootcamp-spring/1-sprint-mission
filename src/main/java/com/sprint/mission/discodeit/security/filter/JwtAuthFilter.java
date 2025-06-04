package com.sprint.mission.discodeit.security.filter;

import com.sprint.mission.discodeit.security.jwt.JwtBlacklist;
import com.sprint.mission.discodeit.security.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;
  private final JwtBlacklist jwtBlacklist;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String auth = request.getHeader("Authorization");
    if (auth == null || !auth.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }
    // 'Bearer ' 제거
    String accessToken = auth.substring(7);

    // JWT 토큰 파싱하여 서명 검증
    if (!jwtTokenProvider.validate(accessToken)) {
      response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid Token");
      return;
    }

    if (jwtBlacklist.contains(accessToken)) {
      response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid Token");
      return;
    }

    // JWT의 Claim 에서 사용자 정보 추출
    Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

    // Spring Security의 SecurityContext에 인증 정보 저장
    SecurityContextHolder.getContext().setAuthentication(authentication);

    filterChain.doFilter(request, response);
  }
}