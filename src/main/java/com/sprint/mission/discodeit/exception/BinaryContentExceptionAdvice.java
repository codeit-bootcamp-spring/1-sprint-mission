package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.binary.BinaryContentNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BinaryContentExceptionAdvice {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(BinaryContentNotFoundException.class)
  public ErrorResponse notFoundFile(BinaryContentNotFoundException e) {

    return ErrorResponse.builder()
        .timestamp(e.getTimestamp())
        .code(e.getErrorCode().getCode())
        .message(e.getMessage())
        .details(e.getDetails())
        .exceptionType(e.getClass().getSimpleName())
        .status(HttpStatus.BAD_REQUEST.value())
        .build();
  }
}
