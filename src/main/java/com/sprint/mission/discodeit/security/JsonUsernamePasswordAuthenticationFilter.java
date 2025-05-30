package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final ObjectMapper objectMapper;

  public JsonUsernamePasswordAuthenticationFilter(
      AuthenticationManager authenticationManager,
      ObjectMapper objectMapper) {
    super(authenticationManager);
    this.objectMapper = objectMapper;
    setFilterProcessesUrl("/api/auth/login"); // 커스텀 로그인 URL
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response)
      throws AuthenticationException {

    try (ServletInputStream inputStream = request.getInputStream()) {
      LoginRequest loginRequest = objectMapper.readValue(inputStream, LoginRequest.class);

      UsernamePasswordAuthenticationToken authRequest =
          new UsernamePasswordAuthenticationToken(
              loginRequest.username(),
              loginRequest.password());

      setDetails(request, authRequest);
      return this.getAuthenticationManager().authenticate(authRequest);

    } catch (IOException e) {
      throw new AuthenticationServiceException("Invalid login request", e);
    }
  }

  public static class Configurer extends AbstractHttpConfigurer<Configurer, HttpSecurity> {

    private final ObjectMapper objectMapper;

    public Configurer(ObjectMapper objectMapper) {
      this.objectMapper = objectMapper;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
      AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

      JsonUsernamePasswordAuthenticationFilter filter =
          new JsonUsernamePasswordAuthenticationFilter(authenticationManager, objectMapper);

      filter.setAuthenticationSuccessHandler((req, res, auth) -> {
        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json");
        res.getWriter().write("{\"message\":\"login success\"}");
      });

      filter.setAuthenticationFailureHandler((req, res, ex) -> {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json");
        res.getWriter().write("{\"message\":\"login failed\"}");
      });

      http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
  }

}

