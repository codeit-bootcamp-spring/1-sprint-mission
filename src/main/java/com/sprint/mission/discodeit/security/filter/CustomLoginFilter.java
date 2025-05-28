package com.sprint.mission.discodeit.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.UserLoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class CustomLoginFilter extends AbstractAuthenticationProcessingFilter {

  private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(
      "/api/auth/login", "POST");
  private final ObjectMapper mapper;
  private boolean postOnly = true;

  public CustomLoginFilter(ObjectMapper mapper) {
    super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    this.mapper = mapper;
  }

  @Override
  public void setSessionAuthenticationStrategy(
      SessionAuthenticationStrategy sessionAuthenticationStrategy) {
    super.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, IOException {
    if (this.postOnly && !request.getMethod().equals("POST")) {
      throw new AuthenticationServiceException(
          "Authentication method not supported: " + request.getMethod());
    }
    String contentType = request.getContentType();
    if (contentType == null || !contentType.toLowerCase().startsWith("application/json")) {
      throw new AuthenticationServiceException(
          "Authentication method not supported: " + request.getContentType()
              + "Expected application/json.");
    }

    UserLoginRequest loginRequest = mapper.readValue(request.getInputStream(),
        UserLoginRequest.class);
    String username = loginRequest.username();
    username = username != null ? username.trim() : "";
    String password = loginRequest.password();
    password = password != null ? password.trim() : "";

    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
        username, password);

    setDetails(request, authRequest);

    return this.getAuthenticationManager().authenticate(authRequest);
  }

  protected void setDetails(HttpServletRequest request,
      UsernamePasswordAuthenticationToken authRequest) {
    authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
  }
}
