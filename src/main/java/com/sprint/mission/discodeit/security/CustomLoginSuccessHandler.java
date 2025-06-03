package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
@Slf4j
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtService jwtService;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {

    if (!(authentication.getPrincipal() instanceof DiscodeitUserDetails userDetails)) {
      log.error("Unexpected principal type: {}", authentication.getPrincipal().getClass().getName());
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected principal type");
      return;
    }

    UserDto userDto = userDetails.getUserDto();
    jwtService.handleLoginSuccess(userDto, response);
  }
}
