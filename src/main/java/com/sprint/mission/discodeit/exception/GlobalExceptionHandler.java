package com.sprint.mission.discodeit.exception;

import java.time.Instant;
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

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
    HttpStatus status = getStatus(e.getErrorCode());
    logException(e, status);

    return ResponseEntity
        .status(status)
        .body(ErrorResponse.from(e, status.value()));
  }

  //입력값 검증 예외 처리
  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException e) {

    Map<String, Object> details = new HashMap<>();
    e.getBindingResult().getFieldErrors().forEach(error ->
        details.put(error.getField(), error.getDefaultMessage()));

    log.warn("Validation error occurred: {}", details, e);

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ErrorResponse.of(
            Instant.now(),
            ErrorCode.VALIDATION_ERROR.name(),
            "Validation failed",
            details,
            e.getClass().getSimpleName(),
            HttpStatus.BAD_REQUEST.value()
        ));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    log.error("Unexpected error occurred: {}", e.getMessage(), e);

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ErrorResponse.of(
            Instant.now(),
            ErrorCode.INTERNAL_SERVER_ERROR.name(),
            "Internal server error",
            Map.of(),
            e.getClass().getSimpleName(),
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        ));
  }

  //errorcode 별 http 상태코드 매핑
  private HttpStatus getStatus(ErrorCode errorCode) {

    return switch (errorCode) {
      case USER_NOT_FOUND, CHANNEL_NOT_FOUND, MESSAGE_NOT_FOUND,
           BINARY_CONTENT_NOT_FOUND, READ_STATUS_NOT_FOUND, USER_STATUS_NOT_FOUND ->
          HttpStatus.NOT_FOUND;

      case DUPLICATE_USER, READ_STATUS_ALREADY_EXISTS, USER_STATUS_ALREADY_EXISTS ->
          HttpStatus.CONFLICT;

      case INVALID_PASSWORD, VALIDATION_ERROR, PRIVATE_CHANNEL_UPDATE -> HttpStatus.BAD_REQUEST;

      default -> HttpStatus.INTERNAL_SERVER_ERROR;
    };
  }

  private void logException(DiscodeitException e, HttpStatus status) {
    if (status.is5xxServerError()) {
      log.error("Server error occurred: [{}] {}",
          e.getErrorCode().name(), e.getMessage(), e);
    } else {
      log.warn("Client error occurred: [{}] {} - Details: {}",
          e.getErrorCode().name(), e.getMessage(), e.getDetails());
    }
  }
}