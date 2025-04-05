package com.sprint.mission.discodeit.global.exception;

import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// Controller + ResponseBody
@Slf4j
@RestControllerAdvice(basePackages = {"com.sprint.mission.discodeit.controller.api"})
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  // 유효성 검사에 대한 예외 처리하기
  @Override
  public ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

    ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;
    FieldError fieldError = ex.getFieldErrors().get(0);

    log.info("{} - {}", errorCode.name(),
        Map.of(fieldError.getField(), fieldError.getRejectedValue()));

    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(Instant.now())
        .code(errorCode.name())
        .message(fieldError.getDefaultMessage())
        .details(Map.of(fieldError.getField(), fieldError.getRejectedValue()))
        .exceptionType(ex.getClass().getSimpleName())
        .status(errorCode.getHttpStatus().value())
        .build();

    return ResponseEntity
        .status(errorResponse.getStatus())
        .body(errorResponse);
  }

  // BusinessException 에 대한 예외 처리하기
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(
      BusinessException ex) {

    ErrorCode errorCode = ex.getErrorCode();
    log.info("{} - code:{}", ex.getClass().getSimpleName(), errorCode.getCode());

    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(ex.getTimestamp())
        .code(errorCode.name())
        .message(errorCode.getMessage())
        .details(ex.getDetails())
        .exceptionType(ex.getClass().getSimpleName())
        .status(errorCode.getHttpStatus().value())
        .build();

    return handleExceptionInternal(errorResponse);
  }

  // 정의된 예외 이외에 모든 예외처리
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleAllException(Exception ex) {
    // TODO : StackTrace 관련 설정 추가
    log.error("Unexpected error occurred", ex);

    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(Instant.now())
        .code(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
        .message(ex.getMessage())
        .exceptionType(ex.getClass().getSimpleName())
        .status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().value())
        .build();

    return handleExceptionInternal(errorResponse);
  }

  private ResponseEntity<ErrorResponse> handleExceptionInternal(ErrorResponse errorResponse) {
    return ResponseEntity
        .status(errorResponse.getStatus())
        .body(errorResponse);
  }

}
