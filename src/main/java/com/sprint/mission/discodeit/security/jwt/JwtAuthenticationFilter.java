package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.security.UserDetailsAdapter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  private final RequestMatcher publicPaths = new OrRequestMatcher(
      new AntPathRequestMatcher("/api/auth/login", "POST"),
      new AntPathRequestMatcher("/api/auth/refresh", "POST"),
      new AntPathRequestMatcher("/api/auth/csrf-token", "GET"),
      new AntPathRequestMatcher("/api/users", "POST"),
      new AntPathRequestMatcher("/css/**"),
      new AntPathRequestMatcher("/js/**"),
      new AntPathRequestMatcher("/images/**"),
      new AntPathRequestMatcher("/webjars/**"),
      new AntPathRequestMatcher("/**favicon.ico"),
      new AntPathRequestMatcher("/"),
      new AntPathRequestMatcher("/index.html"),
      new AntPathRequestMatcher("/assets/**"),
      new AntPathRequestMatcher("/swagger-ui/**"),
      new AntPathRequestMatcher("/v3/api-docs/**"),
      new AntPathRequestMatcher("/actuator/**"),
      new AntPathRequestMatcher("/static/**")
  );

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    if (publicPaths.matches(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      String jwt = extractJwtFromRequest(request);

      if (StringUtils.hasText(jwt)) {
        Optional<UserDto> userDtoOpt = jwtService.validateToken(jwt);

        if (userDtoOpt.isPresent()) {
          UserDto userDto = userDtoOpt.get();

          List<SimpleGrantedAuthority> authorities = userDto.roles().stream()
              .map(SimpleGrantedAuthority::new)
              .collect(Collectors.toList());

          UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(userDto, null, authorities);

          SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
          log.debug("잘못된 JWT token 입니다.");
        }
      } else {
        log.debug("요청된 JWT token을 찾을 수 없습니다.");
      }
    } catch (Exception e) {
      log.error("보안 컨텍스트에서 사용자 인증을 설정할 수 없습니다", e);
    }

    filterChain.doFilter(request, response);
  }

  private String extractJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}