package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.readstatus.ReadStatusExistsException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ReadStatusExceptionAdvice {

  /**
   * 이미 존재함
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ReadStatusExistsException.class)
  public ErrorResponse existsReadStatus(ReadStatusExistsException e) {

    return ErrorResponse.builder()
        .timestamp(e.getTimestamp())
        .code(e.getErrorCode().getCode())
        .message(e.getMessage())
        .details(e.getDetails())
        .exceptionType(e.getClass().getSimpleName())
        .status(HttpStatus.BAD_REQUEST.value())
        .build();
  }

  /**
   * 읽은 상태 찾지 못함
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ReadStatusNotFoundException.class)
  public ErrorResponse notFoundReadStatus(ReadStatusNotFoundException e) {

    return ErrorResponse.builder()
        .timestamp(e.getTimestamp())
        .code(e.getErrorCode().getCode())
        .message(e.getMessage())
        .details(e.getDetails())
        .exceptionType(e.getClass().getSimpleName())
        .status(HttpStatus.NOT_FOUND.value())
        .build();
  }
}
