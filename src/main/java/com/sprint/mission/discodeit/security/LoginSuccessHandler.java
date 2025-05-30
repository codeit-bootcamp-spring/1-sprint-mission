package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final UserMapper userMapper;
  private final ObjectMapper objectMapper;

  public LoginSuccessHandler(UserMapper userMapper, ObjectMapper objectMapper) {
    this.userMapper = userMapper;
    this.objectMapper = objectMapper;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
    User user = principal.getUser();
    UserDto userDto = userMapper.toDto(user);

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json");
    objectMapper.writeValue(response.getWriter(), userDto);
  }
}
