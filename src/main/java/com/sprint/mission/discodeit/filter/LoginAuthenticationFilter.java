package com.sprint.mission.discodeit.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final ObjectMapper mapper = new ObjectMapper();

  public LoginAuthenticationFilter(AuthenticationManager authenticationManager) {
    super.setAuthenticationManager(authenticationManager);
    setFilterProcessesUrl("/api/auth/login");
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws
      AuthenticationException {
    try {
      LoginRequest loginRequest = mapper.readValue(
          request.getInputStream(),
          LoginRequest.class);

      UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
          loginRequest.email(),
          loginRequest.password());

      return this.getAuthenticationManager()
          .authenticate(authRequest);
    } catch (IOException e) {
      throw new AuthenticationServiceException(
          "Invalid login request",
          e);
    }
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult
  ) throws
      IOException,
      ServletException {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authResult);
    SecurityContextHolder.setContext(context);

    SecurityContextRepository repo = new HttpSessionSecurityContextRepository();
    repo.saveContext(
        context,
        request,
        response);

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json");
    response.getWriter()
        .write(mapper.writeValueAsString(authResult.getPrincipal()));
  }

  @Override
  protected void unsuccessfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException failed
  ) throws
      IOException,
      ServletException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter()
        .write("{\"error\": \"Invalid credentials\"}");
  }
}
