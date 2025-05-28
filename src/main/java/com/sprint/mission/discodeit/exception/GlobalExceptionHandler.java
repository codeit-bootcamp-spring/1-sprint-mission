package com.sprint.mission.discodeit.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> expectedException(DiscodeitException e) {
    log.error("error: ", e);
    ErrorResponse errorResponse = ErrorResponse.fromDiscodeitException(e);
    return ResponseEntity.status(errorResponse.status()).body(errorResponse);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> methodArgumentNotValidException(
      MethodArgumentNotValidException e
  ) {
    Map<String, Object> details = new HashMap<>();
    e.getBindingResult().getFieldErrors().forEach(fieldError -> {
      details.put(fieldError.getField(), fieldError.getDefaultMessage());
    });
    ErrorResponse errorResponse = ErrorResponse.fromException(e, HttpStatus.BAD_REQUEST.value());
    return ResponseEntity.status(errorResponse.status()).body(errorResponse);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> exception(RuntimeException e) {
    log.error("error: ", e);
    ErrorResponse errorResponse = ErrorResponse.fromException(e, HttpStatus.INTERNAL_SERVER_ERROR.value());
    return ResponseEntity.status(errorResponse.status()).body(errorResponse);
  }
}
