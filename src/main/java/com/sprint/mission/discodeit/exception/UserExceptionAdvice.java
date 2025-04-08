package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateUsernameException;
import com.sprint.mission.discodeit.exception.user.IllegalUserException;
import com.sprint.mission.discodeit.exception.user.UserAuthException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
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
public class UserExceptionAdvice {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(UserNotFoundException.class)
  public ErrorResponse userNotFound(UserNotFoundException e) {
    log.error("User exception: {}", e.getMessage());

    return ErrorResponse.builder()
        .timestamp(e.getTimestamp())
        .code(e.getErrorCode().getCode())
        .message(e.getMessage())
        .details(e.getDetails())
        .exceptionType(e.getClass().getSimpleName())
        .status(HttpStatus.NOT_FOUND.value())
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalUserException.class)
  public ErrorResponse userIllegal(IllegalUserException e) {
    log.error("User Illegal exception: {}", e.getMessage());

    return ErrorResponse.builder()
        .timestamp(e.getTimestamp())
        .code(e.getErrorCode().getCode())
        .message(e.getMessage())
        .details(e.getDetails())
        .exceptionType(e.getClass().getSimpleName())
        .status(HttpStatus.BAD_REQUEST.value())
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(UserAuthException.class)
  public ErrorResponse userAuth(UserAuthException e) {
    log.error("User Illegal exception: {}", e.getMessage());

    return ErrorResponse.builder()
        .timestamp(e.getTimestamp())
        .code(e.getErrorCode().getCode())
        .message(e.getMessage())
        .details(e.getDetails())
        .exceptionType(e.getClass().getSimpleName())
        .status(HttpStatus.BAD_REQUEST.value())
        .build();
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(DuplicateUsernameException.class)
  public ErrorResponse existsUser(DuplicateUsernameException e) {
    log.error("User exists exception: {}", e.getMessage());

    return ErrorResponse.builder()
        .timestamp(e.getTimestamp())
        .code(e.getErrorCode().getCode())
        .message(e.getMessage())
        .details(e.getDetails())
        .exceptionType(e.getClass().getSimpleName())
        .status(HttpStatus.CONFLICT.value())
        .build();
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(DuplicateEmailException.class)
  public ErrorResponse existsEmail(DuplicateEmailException e) {
    log.error("User email exists exception: {}", e.getMessage());

    return ErrorResponse.builder()
        .timestamp(e.getTimestamp())
        .code(e.getErrorCode().getCode())
        .message(e.getMessage())
        .details(e.getDetails())
        .exceptionType(e.getClass().getSimpleName())
        .status(HttpStatus.CONFLICT.value())
        .build();
  }
}
