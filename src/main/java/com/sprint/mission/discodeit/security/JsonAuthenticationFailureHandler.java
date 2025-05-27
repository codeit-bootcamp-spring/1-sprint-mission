package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@RequiredArgsConstructor
public class JsonAuthenticationFailureHandler implements AuthenticationFailureHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException {

    Map<String, String> errorResponse = new HashMap();
    errorResponse.put("error", "Authentication failed");
    errorResponse.put("message", exception.getMessage());

    objectMapper.writeValue(response.getWriter(), errorResponse);
  }
}
