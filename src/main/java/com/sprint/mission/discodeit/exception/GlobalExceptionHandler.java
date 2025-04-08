package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import com.sprint.mission.discodeit.dto.response.ErrorResponse;
import com.sprint.mission.discodeit.exception.authentication.WrongPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
            ex.getTimestamp(),
            ex.getErrorCode().toString(),
            ex.getMessage(),
            ex.getDetails(),
            ex.getClass().getSimpleName(),
            ex.getErrorCode().getHttpStatus().value()
    );
    return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidException(MethodArgumentNotValidException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
            Instant.now(),
            "VALIDATION_FAILED",
            ex.getMessage(),
            null,
            ex.getClass().getSimpleName(),
            HttpStatus.BAD_REQUEST.value()
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    Map<String, Object> details = new HashMap<>();
    details.put("stackTrace", e.getStackTrace());
    ErrorResponse errorResponse = new ErrorResponse(
            Instant.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.toString(),
            e.getMessage(),
            details,
            e.getClass().getSimpleName(),
            HttpStatus.INTERNAL_SERVER_ERROR.value()
    );
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
}
