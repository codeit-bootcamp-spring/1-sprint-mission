package com.sprint.mission.discodeit.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
    log.warn("인증되지 않은 접근: {} - {}", request.getRequestURI(), authException.getMessage());

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json;charset=UTF-8");

    ExceptionDto error = ExceptionDto.builder()
        .timestamp(Instant.now())
        .httpCode(HttpStatus.UNAUTHORIZED)
        .code("AUTHENTICATION_REQUIRED")
        .message("인증이 필요합니다. 로그인 해 주세요.")
        .exceptionType(authException.getClass().getSimpleName())
        .build();

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    response.getWriter().write(mapper.writeValueAsString(error));

  }
}
