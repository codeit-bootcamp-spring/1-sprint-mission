package com.sprint.mission.discodeit.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiscodeitAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {

    ErrorResponse error = ErrorResponse.builder()
        .timestamp(Instant.now())
        .code(HttpStatus.FORBIDDEN.toString())
        .message(ErrorCode.FORBIDDEN.getMessage())
        .details(
            Map.of(
                HttpStatus.FORBIDDEN.toString(),
                accessDeniedException.getMessage()
            )
        )
        .exceptionType(ErrorCode.FORBIDDEN.getCode())
        .status(HttpStatus.FORBIDDEN.value())
        .build();

    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    objectMapper.writeValue(response.getWriter(), error);

  }
}
