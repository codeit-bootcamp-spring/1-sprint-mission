package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.exception.ErrorResponse;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.UserMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final UserMapper userMapper;
  private final AuthenticationManager authenticationManager;
  private final SecurityContextRepository securityContextRepository;

  public CustomAuthenticationFilter(AuthenticationManager authenticationManager,
      SecurityContextRepository securityContextRepository, UserMapper userMapper) {
    super(authenticationManager);
    this.userMapper = userMapper;
    this.authenticationManager = authenticationManager;
    this.securityContextRepository = securityContextRepository;
    setFilterProcessesUrl("/api/auth/login");
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {

    if (!request.getMethod().equals("POST")) {
      throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
    }

    try {
      LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

      UsernamePasswordAuthenticationToken token =
          new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());

      return authenticationManager.authenticate(token);
    } catch (IOException e) {
      throw new RuntimeException("로그인 요청 파싱 실패", e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult) throws IOException {

    SecurityContextHolder.getContext().setAuthentication(authResult);
    securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);

    // 성공 응답
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
    User user = userDetails.getUser();
    UserDto userDto = userMapper.toDto(user);

    objectMapper.writeValue(response.getWriter(), userDto);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException failed) throws IOException {

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    DiscodeitException e = new DiscodeitException(ErrorCode.USER_NOT_FOUND, new HashMap<>());
    ErrorResponse errorResponse = ErrorResponse.from(e);
    objectMapper.writeValue(response.getWriter(), errorResponse);
  }


}
