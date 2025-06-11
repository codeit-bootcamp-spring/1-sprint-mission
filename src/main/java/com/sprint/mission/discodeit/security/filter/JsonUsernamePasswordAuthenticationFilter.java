package com.sprint.mission.discodeit.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.security.SecurityMatchers;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@RequiredArgsConstructor
public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final ObjectMapper objectMapper;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {

    if (!request.getMethod().equals("POST")) {
      throw new AuthenticationServiceException(
          "Authentication method not supported: " + request.getMethod());
    }

    try {
      // JSON 요청 본문 파싱
      LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(),
          LoginRequest.class);

      UsernamePasswordAuthenticationToken authRequest =
          new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());

      setDetails(request, authRequest);
      return this.getAuthenticationManager().authenticate(authRequest);

    } catch (IOException e) {
      throw new AuthenticationServiceException("Request parsing failed", e);
    }
  }


  public static class Configurer extends
      AbstractAuthenticationFilterConfigurer<HttpSecurity, Configurer, JsonUsernamePasswordAuthenticationFilter> {

    public Configurer(ObjectMapper objectMapper) {
      super(new JsonUsernamePasswordAuthenticationFilter(objectMapper), SecurityMatchers.LOGIN_URL);
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
      return new AntPathRequestMatcher(loginProcessingUrl, HttpMethod.POST.name());
    }

    @Override
    public void init(HttpSecurity http) throws Exception {
      loginProcessingUrl(SecurityMatchers.LOGIN_URL);
    }
  }
}
