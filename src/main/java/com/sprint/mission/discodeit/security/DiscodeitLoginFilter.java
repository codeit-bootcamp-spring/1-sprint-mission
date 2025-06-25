package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.UserLoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RequiredArgsConstructor
public class DiscodeitLoginFilter extends UsernamePasswordAuthenticationFilter {

  private final ObjectMapper objectMapper;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    log.debug("JsonUsernamePasswordAuthenticationFilter 호출");
    UserLoginRequest userLoginRequest = null;
    try {
      userLoginRequest = objectMapper.readValue(request.getInputStream(),
          UserLoginRequest.class);
    } catch (IOException e) {
      throw new AuthenticationServiceException("Request parsing failed", e);
    }

    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
        userLoginRequest.username(), userLoginRequest.password());
    return this.getAuthenticationManager().authenticate(token);
  }

  public static class Configurer extends
      AbstractAuthenticationFilterConfigurer<HttpSecurity, Configurer, DiscodeitLoginFilter> {

    public Configurer(ObjectMapper objectMapper) {
      super(new DiscodeitLoginFilter(objectMapper), SecurityMatchers.LOGIN_URL);
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
      return new AntPathRequestMatcher(loginProcessingUrl, HttpMethod.POST.name());
    }
  }
}
