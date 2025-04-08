package com.sprint.mission.discodeit.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        Instant.now(),
        ex.getErrorCode().getCode(),
        ex.getMessage(),
        ex.getDetails(),
        ex.getClass().getSimpleName(),
        HttpStatus.BAD_REQUEST.value()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        Instant.now(),
        ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
        ErrorCode.INTERNAL_SERVER_ERROR.getMessage(),
        Map.of("error", ex.getMessage()),
        ex.getClass().getSimpleName(),
        HttpStatus.INTERNAL_SERVER_ERROR.value()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, Object> errors = new HashMap<>();

    ex.getBindingResult().getFieldErrors().forEach(error -> {
      String fieldName = error.getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    ErrorResponse errorResponse = new ErrorResponse(
        Instant.now(),
        "VALIDATION_ERROR",
        "요청 데이터 유효성 검증 실패",
        errors,
        ex.getClass().getSimpleName(),
        HttpStatus.BAD_REQUEST.value()
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

}