package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.response.ErrorResponse;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 커스텀 예외 (DiscodeitException)
   */
  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
    ErrorCode errorCode = e.getErrorCode();
    ErrorResponse response = new ErrorResponse(
        e.getTimestamp(),
        errorCode.getCode(),
        errorCode.getMessage(),
        e.getDetails(),
        e.getClass().getSimpleName(),
        errorCode.getStatus().value()
    );
    return ResponseEntity.status(errorCode.getStatus()).body(response);
  }

  /**
   * 잘못된 입력 예외 (예: 유효하지 않은 파라미터)
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
    ErrorResponse response = new ErrorResponse(
        Instant.now(),
        "COMMON_400",
        e.getMessage(),
        null,
        e.getClass().getSimpleName(),
        HttpStatus.BAD_REQUEST.value()
    );
    return ResponseEntity.badRequest().body(response);
  }

  /**
   * 리소스 찾을 수 없음 (예: ID에 해당하는 유저 없음)
   */
  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
    ErrorResponse response = new ErrorResponse(
        Instant.now(),
        "COMMON_404",
        e.getMessage(),
        null,
        e.getClass().getSimpleName(),
        HttpStatus.NOT_FOUND.value()
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  /**
   * 모든 예외의 마지막  - 서버 내부 에러
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleUnhandledException(Exception e) {
    ErrorResponse response = new ErrorResponse(
        Instant.now(),
        "COMMON_500",
        "서버 내부 오류가 발생했습니다.",
        Map.of("message", e.getMessage()),
        e.getClass().getSimpleName(),
        HttpStatus.INTERNAL_SERVER_ERROR.value()
    );
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    Map<String, Object> errors = e.getBindingResult()
        .getFieldErrors()
        .stream()
        .collect(Collectors.toMap(
            FieldError::getField,
            FieldError::getDefaultMessage,
            (existing, replacement) -> existing));
    ErrorResponse response = new ErrorResponse(
        Instant.now(),
        "VALIDATION_FAILED",
        "입력값이 유효하지 않습니다.",
        errors,
        e.getClass().getSimpleName(),
        400
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }
}
