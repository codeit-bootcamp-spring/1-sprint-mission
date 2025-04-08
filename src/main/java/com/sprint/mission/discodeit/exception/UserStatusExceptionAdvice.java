package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.userstatus.UserStatusNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserStatusExceptionAdvice {

  /**
   * 유저 상태 찾지 못했을 떄
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(UserStatusNotFoundException.class)
  public ErrorResponse notfoundUserStatus(UserStatusNotFoundException e) {
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
