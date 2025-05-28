package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
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
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

  private final ObjectMapper objectMapper;
  private final JwtService jwtService;

  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    DiscodeitUserDetails principal = (DiscodeitUserDetails) authentication.getPrincipal();

    JwtSession jwtSession = jwtService.generateTokens(principal.getUserDto());

    Cookie refreshTokenCookie = new Cookie("refreshToken", jwtSession.getRefreshToken());
    refreshTokenCookie.setHttpOnly(true); //xss 방지
    refreshTokenCookie.setSecure(false); // HTTPS에서만 전송할 수 있도록 하는 설정(운영에서는 true)
    refreshTokenCookie.setPath("/"); //전체경로에서 접근 가능
    refreshTokenCookie.setMaxAge(60 * 60 * 24 * 30);//30일

    response.addCookie(refreshTokenCookie);

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    response.getWriter().write(objectMapper.writeValueAsString(jwtSession.getAccessToken()));
  }
}
