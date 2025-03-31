package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
    ErrorCode code = e.getErrorCode();
    HttpStatus status = HttpStatus.valueOf(code.getStatus());

    ErrorResponse response = new ErrorResponse(
        e.getTimestamp(),
        code.getCode(),
        code.getMessage(),
        e.getDetails(),
        e.getClass().getSimpleName(),
        code.getStatus()
    );

    return ResponseEntity.status(status).body(response);
  }

}
