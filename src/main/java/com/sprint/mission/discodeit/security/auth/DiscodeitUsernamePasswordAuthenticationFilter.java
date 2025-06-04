package com.sprint.mission.discodeit.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.auth.LoginDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.error.ErrorResponse;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.service.basic.UserOnlineStatusService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@Slf4j
public class DiscodeitUsernamePasswordAuthenticationFilter extends
    UsernamePasswordAuthenticationFilter {

  private final ObjectMapper mapper = new ObjectMapper();
  private final UserMapper userMapper;
  private final SessionRegistry sessionRegistry;
  private final RememberMeServices rememberMeServices;
  private final UserOnlineStatusService statusService;
  private final JwtService jwtService;
  //  @Value("${app.jwt.refresh-token-expiration}")
  private final long refreshTokenExpiration;

  public DiscodeitUsernamePasswordAuthenticationFilter(UserMapper userMapper,
      SessionRegistry registry, RememberMeServices rememberMeServices,
      UserOnlineStatusService statusService, JwtService jwtService, long refreshTokenExpiration) {
    setFilterProcessesUrl("/api/auth/login");
    this.userMapper = userMapper;
    this.sessionRegistry = registry;
    this.rememberMeServices = rememberMeServices;
    this.statusService = statusService;
    this.jwtService = jwtService;
    this.refreshTokenExpiration = refreshTokenExpiration;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    try {
      LoginDto login = mapper.readValue(request.getInputStream(), LoginDto.class);
      UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
          login.username(), login.password());
      setDetails(request, authRequest);

      return this.getAuthenticationManager().authenticate(authRequest);
    } catch (IOException e) {
      log.info("AUTHENTICATION FAILED : reason={}", e.getMessage());
      throw new DisabledException("LOGIN FAILED");
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, FilterChain chain, Authentication authResult)
      throws IOException, ServletException {

    SecurityContextHolder.getContext().setAuthentication(authResult);

    new HttpSessionSecurityContextRepository().saveContext(SecurityContextHolder.getContext(),
        request, response);

    // rememberMeServices.loginSuccess(request, response, authResult);

    DiscodeitUserDetails userDetails = (DiscodeitUserDetails) authResult.getPrincipal();
    User user = userDetails.getUser();
    UserDto dto = userMapper.toDto(user, statusService);

    JwtSession jwtSession = jwtService.generateJwtSession(dto);

    // sessionRegistry.registerNewSession(request.getSession().getId(), authResult.getPrincipal());

    Cookie refreshToken = new Cookie("refresh_token", jwtSession.getRefreshToken());
    refreshToken.setHttpOnly(true);
    refreshToken.setSecure(true);
    refreshToken.setPath("/");
    refreshToken.setMaxAge((int) (refreshTokenExpiration / 1000));
    response.addCookie(refreshToken);

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    mapper.writeValue(response.getWriter(), jwtSession.getAccessToken());

//    response.setStatus(HttpServletResponse.SC_OK);
//    response.setContentType("application/json");
//    response.setCharacterEncoding("UTF-8");
//    mapper.writeValue(response.getWriter(), dto);
  }


  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed)
      throws IOException, ServletException {
    log.warn("Login failed: {}", failed.getMessage(), failed);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    ErrorResponse errorResponse = new ErrorResponse(
        Instant.now(),
        "",
        "Authorization Failed",
        Map.of(),
        "AuthorizationException.class",
        401
    );
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    mapper.writeValue(response.getWriter(), errorResponse);
  }
}
