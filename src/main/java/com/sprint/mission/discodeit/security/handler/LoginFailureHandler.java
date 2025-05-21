package com.sprint.mission.discodeit.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.exception.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    log.info("로그인 실패");

    sendErrorResponse(
        response,
        HttpStatus.UNAUTHORIZED,
        "UNAUTHORIZED_USER",
        "로그인 인증에 실패했습니다.",
        Map.of(),
        exception.getClass().getSimpleName()
    );
  }


  private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String code,
      String message, Map<String, Object> details, String exceptionType) throws IOException {
    response.setStatus(status.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");

    ErrorResponse errorResponse = new ErrorResponse(
        Instant.now(),
        code,
        message,
        details,
        exceptionType,
        status.value()
    );

    objectMapper.writeValue(response.getWriter(), errorResponse);
  }
}
