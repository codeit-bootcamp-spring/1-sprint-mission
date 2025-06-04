package com.sprint.mission.discodeit.exception;


import com.sprint.mission.discodeit.dto.ErrorResponse;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(
      final DiscodeitException exception) {
    return ResponseEntity
        .status(exception.getErrorCode().getHttpStatus())
        .body(ErrorResponse.from(exception));
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException e) {
    return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse.from(e, HttpServletResponse.SC_UNAUTHORIZED));
  }

  //valid 에러
  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException e) {
    ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;

    // 필드별 에러 메시지 리스트 생성
    List<String> errors = e.getBindingResult().getFieldErrors().stream()
        .map(err -> err.getField() + ": " + err.getDefaultMessage())
        .toList();

    ErrorResponse response = ErrorResponse.of(
        errorCode,
        Map.of("errors", errors),
        e.getClass()
    );

    return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
  }

  //잘못된 http method 요청
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  protected ResponseEntity<ErrorResponse> handleMethodNotAllowed(
      final HttpRequestMethodNotSupportedException e) {
    ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;
    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(ErrorResponse.of(errorCode, Map.of("method", e.getMethod()), e.getClass()));
  }

  //그 외 모든 에러
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleUnknownException(final Exception e) {
    return ResponseEntity
        .status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
        .body(ErrorResponse.of(
            ErrorCode.INTERNAL_SERVER_ERROR,
            Map.of("error", e.getMessage()),
            e.getClass()
        ));
  }


}
