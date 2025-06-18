package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.View;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private final View error;

  public GlobalExceptionHandler(View error) {
    this.error = error;
  }

  // DiscodeitException 을 처리하는 핸들러
  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        ex.getTimestamp(),
        ex.getErrorCode().name(), // ErrorCode Enum에서 코드명 추출
        ex.getErrorCode().getMessage(),
        ex.getDetails(),
        ex.getClass().getSimpleName(), // 예외 클래스 이름
        ex.getErrorCode().getStatus().value()
    );
    return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
  }

  /* 해당 예외 발생시 응답 에상
    {
      "timestamp": "2025-03-31T12:34:56.789Z",
      "errorCode": "VALIDATION_FAILED",
      "message": "입력값이 유효하지 않습니다.",
      "details": {
        "username": "username은 비어 있을 수 없습니다.",
        "email": "올바른 이메일 형식이어야 합니다.",
        "password": "password는 공백일 수 없습니다."
      },
      "exception": "MethodArgumentNotValidException",
      "status": 400
    }
   */
  // MethodArgumentNotValidException (Spring 입력 값 검증 예외) 을 처리하는 핸들러
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, Object> errors = new HashMap<>();

    // DTO 필드별 검증 실패 메시지 가져오기
    ex.getBindingResult().getFieldErrors().forEach(
        error -> errors.put(error.getField(), error.getDefaultMessage())
    );

    ErrorResponse errorResponse = new ErrorResponse(
        Instant.now(),
        ErrorCode.VALIDATION_FAILED.name(),
        ErrorCode.VALIDATION_FAILED.getMessage(),
        errors,
        ex.getClass().getSimpleName(), // 예외 클래스 이름
        HttpStatus.BAD_REQUEST.value()
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }


  // NullPointerException 를 처리하는 핸들러
  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        Instant.now(),
        "NULL_POINTER_EXCEPTION",
        "예상치 못한 null이 발생했습니다.",
        null,
        ex.getClass().getSimpleName(),
        HttpStatus.INTERNAL_SERVER_ERROR.value()
    );
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  // IllegalArgumentException 를 처리하는 핸들러
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        Instant.now(),
        "ILLEGAL_ARGUMENT_EXCEPTION",
        "허용되지 않은 값입니다.",
        null,
        ex.getClass().getSimpleName(),
        HttpStatus.BAD_REQUEST.value()
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  // 모든 예외를 처리하는 핸들러
  @ExceptionHandler({Exception.class, RuntimeException.class})
  public ResponseEntity<ErrorResponse> handleGeneralExceptions(Exception ex) {

    ErrorResponse errorResponse = new ErrorResponse(
        Instant.now(),
        "UNKNOWN_ERROR",
        "알 수 없는 오류가 발생했습니다.",
        null,
        ex.getClass().getSimpleName(),
        HttpStatus.INTERNAL_SERVER_ERROR.value()
    );

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

}
