package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.AuthenticationRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@AllArgsConstructor
@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final ObjectMapper objectMapper;
  private SessionRegistry sessionRegistry;

  public AuthenticationFilter(AuthenticationManager manager, ObjectMapper objectMapper) {
    super(manager);
    this.objectMapper = objectMapper;
    setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/auth/login", "POST"));
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response)
      throws AuthenticationException {
    if (!request.getMethod().equals("POST")) {
      throw new AuthenticationServiceException(
          "Authentication method not supported: " + request.getMethod());
    }

    try {
      AuthenticationRequest authenticationRequest = objectMapper.readValue(request.getInputStream(),
          AuthenticationRequest.class);
      UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
          authenticationRequest.email(), authenticationRequest.password());

      setDetails(request, authRequest);
      return this.getAuthenticationManager().authenticate(authRequest);
    } catch (IOException e) {
      throw new AuthenticationServiceException("Failed to parse authentication request body", e);
    }
  }

  @Override
  public void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authentication) throws IOException, ServletException {

    super.successfulAuthentication(request, response, chain, authentication);

    if (sessionRegistry != null) {
      sessionRegistry.registerNewSession(request.getSession().getId(),
          authentication.getPrincipal());
    }
  }
}
