package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
public class JsonAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final ObjectMapper objectMapper;
  private final UserMapper userMapper;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    User user = userDetails.getUser();

    UserDto userDto = userMapper.toDto(user);
    response.setStatus(HttpStatus.OK.value());
    response.setContentType(MediaType.MULTIPART_FORM_DATA_VALUE);

    objectMapper.writeValue(response.getWriter(), userDto);
  }
}
