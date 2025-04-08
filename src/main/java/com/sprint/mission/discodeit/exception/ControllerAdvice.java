package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ControllerAdvice {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  private ErrorResponse failValidation(MethodArgumentNotValidException e) {

    return ErrorResponse.builder()
        .timestamp(Instant.now())
        .code(ErrorCode.FAIL_BEAN_VALIDATE.getCode())
        .message(ErrorCode.FAIL_BEAN_VALIDATE.getMessage())
        .details(
            Map.of(e.getClass().getSimpleName(), ErrorCode.FAIL_BEAN_VALIDATE.getMessage()))
        .exceptionType(e.getClass().getSimpleName())
        .status(HttpStatus.BAD_REQUEST.value())
        .build();
  }
}
