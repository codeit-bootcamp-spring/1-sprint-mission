package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.auth.DiscodeitUserDetails;
import com.sprint.mission.discodeit.security.auth.DiscodeitUserDetailsService;
import com.sprint.mission.discodeit.service.basic.UserOnlineStatusService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserMapper userMapper;
  private final UserOnlineStatusService userOnlineStatusService;
  private final DiscodeitUserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String token = request.getHeader("Authorization");
    if (token != null && token.startsWith("Bearer")) {

      try {
        token = token.substring(7);
        if (!jwtService.validateToken(token)) {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          return;
        }

        String username = jwtService.extractUsername(token);
        DiscodeitUserDetails userDetails = (DiscodeitUserDetails) userDetailsService.loadUserByUsername(
            username);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (Exception e) {
        log.error("Authentication Failed :{}", e.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }
    }

    filterChain.doFilter(request, response);
  }
}
