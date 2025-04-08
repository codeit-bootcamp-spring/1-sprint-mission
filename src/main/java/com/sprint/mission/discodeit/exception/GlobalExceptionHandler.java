package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.response.ErrorResponse;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

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
  // MethodArgumentNotValidException 을 처리하는 핸들러
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
        "VALIDATION_FAILED", // ErrorCoud Enum 의 코드명과 동일하게
        "입력값이 유효하지 않습니다.",
        errors,
        ex.getClass().getSimpleName(), // 예외 클래스 이름
        HttpStatus.BAD_REQUEST.value()
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

}
