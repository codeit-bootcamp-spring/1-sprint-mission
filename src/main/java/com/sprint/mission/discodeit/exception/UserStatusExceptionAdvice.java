package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.userstatus.UserStatusNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserStatusExceptionAdvice {

  /**
   * 유저 상태 찾지 못했을 떄
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(UserStatusNotFoundException.class)
  public ErrorResponse notfoundUserStatus(UserStatusNotFoundException e) {
    return new ErrorResponse(e.getMessage());
  }
}
