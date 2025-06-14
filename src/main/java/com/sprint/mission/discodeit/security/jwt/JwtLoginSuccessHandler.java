package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

  private final ObjectMapper objectMapper;
  private final JwtService jwtService;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    DiscodeitUserDetails principal = (DiscodeitUserDetails) authentication.getPrincipal();
    jwtService.invalidateJwtSession(principal.getUserDto().id());
    JwtSession jwtSession = jwtService.registerJwtSession(principal.getUserDto());

    String refreshToken = jwtSession.getRefreshToken();
    Cookie refreshTokenCookie = new Cookie(JwtService.REFRESH_TOKEN_COOKIE_NAME, refreshToken);
    refreshTokenCookie.setHttpOnly(true);
    response.addCookie(refreshTokenCookie);

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(objectMapper.writeValueAsString(jwtSession.getAccessToken()));
  }
}
