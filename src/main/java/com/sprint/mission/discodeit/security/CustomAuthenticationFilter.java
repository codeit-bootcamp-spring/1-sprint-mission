package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.auth.LoginResponse;
import com.sprint.mission.discodeit.dto.exception.ErrorResponse;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextRepository;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  @Autowired
  private RememberMeServices rememberMeServices;
  @Autowired
  private JwtService jwtService;

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final UserMapper userMapper;
  private final AuthenticationManager authenticationManager;
  private final SecurityContextRepository securityContextRepository;
  private final SessionAuthenticationStrategy sessionAuthenticationStrategy;

  public CustomAuthenticationFilter(AuthenticationManager authenticationManager,
      SecurityContextRepository securityContextRepository, UserMapper userMapper,
      SessionAuthenticationStrategy sessionAuthenticationStrategy) {
    super(authenticationManager);
    this.userMapper = userMapper;
    this.authenticationManager = authenticationManager;
    this.securityContextRepository = securityContextRepository;
    this.sessionAuthenticationStrategy = sessionAuthenticationStrategy;

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

    try {
      sessionAuthenticationStrategy.onAuthentication(authResult, request, response);
    } catch (Exception e) {
      logger.warn("Session authentication strategy failed", e);
    }

    String remember = request.getParameter("remember-me");
    if ("true".equals(remember)) {
      rememberMeServices.loginSuccess(request, response, authResult);
    }

    CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
    User user = userDetails.getUser();
    UserDto userDto = userMapper.toDto(user);

    JwtToken jwtToken = jwtService.generateToken(userDto);

    LoginResponse loginResponse = new LoginResponse(jwtToken.accessToken(), userDto);

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    objectMapper.writeValue(response.getWriter(), loginResponse);

    Cookie refreshCookie = new Cookie("refresh-token", jwtToken.refreshToken());
    refreshCookie.setHttpOnly(true);
    refreshCookie.setPath("/");
    refreshCookie.setMaxAge(2592000);
    response.addCookie(refreshCookie);
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
