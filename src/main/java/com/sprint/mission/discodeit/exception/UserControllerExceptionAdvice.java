package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.user.IllegalUserException;
import com.sprint.mission.discodeit.exception.user.UserAuthException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserControllerExceptionAdvice {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(UserNotFoundException.class)
  public ErrorResult userNotFound(UserNotFoundException e) {
    log.error("User exception: {}", e.getMessage());

    return new ErrorResult(ErrorCode.USER_NOT_FOUND);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalUserException.class)
  public ErrorResult userIllegal(IllegalUserException e) {
    log.error("User Illegal exception: {}", e.getMessage());

    return new ErrorResult(ErrorCode.ILLEGAL_USER);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(UserAuthException.class)
  public ErrorResult userAuth(UserAuthException e) {
    log.error("User Illegal exception: {}", e.getMessage());

    return new ErrorResult(ErrorCode.USER_AUTH_FAIL);
  }
}
