package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  // 커스텀 에러
  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {

    log.error("커스텀 예외 발생: code={}, message={}", e.getErrorCode(), e.getMessage());
    ErrorResponse response = new ErrorResponse(e);

    return ResponseEntity
        .status(response.getStatus())
        .body(response);
  }

  // 유효성 검사 에러
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException e) {

    log.error("요청 유효성 검사 실패: {}", e.getMessage());

    Map<String, Object> validationErrors = new HashMap<>();
    e.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      validationErrors.put(fieldName, errorMessage);
    });

    ErrorResponse response = new ErrorResponse(
        Instant.now(),
        ErrorCode.VALIDATION_ERROR.name(),
        ErrorCode.VALIDATION_ERROR.getMessage(),
        validationErrors,
        e.getClass().getSimpleName(),
        ErrorCode.VALIDATION_ERROR.getStatus()
    );

    return ResponseEntity
        .status(e.getStatusCode())
        .body(response);
  }

  // 서버 에러
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {

    log.error("예상치 못한 오류 발생: {}", e.getMessage(), e);
    ErrorResponse errorResponse = new ErrorResponse(e, ErrorCode.INTERNAL_SERVER_ERROR.getStatus());

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(errorResponse);
  }
}
