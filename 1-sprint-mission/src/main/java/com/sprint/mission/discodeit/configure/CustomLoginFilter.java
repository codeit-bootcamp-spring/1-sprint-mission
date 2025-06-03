package com.sprint.mission.discodeit.configure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;


@Component
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

  private final UserMapper userMapper;

  public CustomLoginFilter(AuthenticationManager authenticationManager, UserMapper userMapper,
      ObjectMapper objectMapper) {
    setAuthenticationManager(authenticationManager);
    setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/auth/login", "POST"));
    this.objectMapper = objectMapper;
    this.userMapper = userMapper; // 🔴 이 줄이 없어서 NPE or 빨간불 발생
  }


  private final ObjectMapper objectMapper;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    try (InputStream is = request.getInputStream()) {
      LoginRequest loginRequest = objectMapper.readValue(is, LoginRequest.class);
      UsernamePasswordAuthenticationToken authRequest =
          new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
      return this.getAuthenticationManager().authenticate(authRequest);
    } catch (IOException e) {
      throw new AuthenticationServiceException("Invalid login request", e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException {
    User user = (User) authResult.getPrincipal();

    UserDto userDto = userMapper.toDto(user);
    response.setContentType("application/json");
    new ObjectMapper().writeValue(response.getOutputStream(), userDto);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException failed) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    response.getWriter().write("{\"error\": \"Unauthorized\"}");
  }
}