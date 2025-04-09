package com.sprint.mission.discodeit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//TODO: 전역예외 설정할 것
@RestControllerAdvice //전역에서 발생하는 예외를 처리한다
public class GlobalExceptionHandler {

  @ExceptionHandler(LoginFailedException.class)
  public ResponseEntity<ErrorResponse> loginFailedException(LoginFailedException e) {
    ErrorResponse response = new ErrorResponse(e.getErrorCode().getStatus(),
        e.getClass().getSimpleName(), e.getErrorCode().name(), e.getErrorCode().getMessage(),
        e.getDetails());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
  }
}

