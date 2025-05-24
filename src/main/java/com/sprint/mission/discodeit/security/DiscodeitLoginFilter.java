package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.UserLoginRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.mapper.UserMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DiscodeitLoginFilter extends AbstractAuthenticationProcessingFilter {

  private final ObjectMapper objectMapper;
  private final UserMapper userMapper;

  @Autowired
  public DiscodeitLoginFilter(
      AuthenticationManager authenticationManager,
      ObjectMapper objectMapper,
      UserMapper userMapper
  ) {
    super(new AntPathRequestMatcher("/api/auth/login", "POST"), authenticationManager);
    this.objectMapper = objectMapper;
    this.userMapper = userMapper;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, IOException {
    log.debug("JsonUsernamePasswordAuthenticationFilter 호출");
    UserLoginRequest userLoginRequest = objectMapper.readValue(request.getInputStream(),
        UserLoginRequest.class);

    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
        userLoginRequest.username(), userLoginRequest.password());
    return this.getAuthenticationManager().authenticate(token);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult)
      throws IOException, ServletException {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authResult);

    SecurityContextRepository repo = new HttpSessionSecurityContextRepository();
    repo.saveContext(context, request, response);

    DiscodeitUserDetails principal = (DiscodeitUserDetails) authResult.getPrincipal();

    UserResponse userResponse  = userMapper.toDto(principal.getUser());
    response.setStatus(HttpStatus.OK.value());
    response.setContentType("application/json");
    objectMapper.writeValue(response.getWriter(), userResponse);
  }
}
