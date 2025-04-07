package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sprint.mission.discodeit.dto.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleException(IllegalArgumentException e) {
    e.printStackTrace();
    return buildErrorResponse(e, HttpStatus.BAD_REQUEST, "INVALID_ARGUMENT");
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorResponse> handleException(NoSuchElementException e) {
    e.printStackTrace();
    return buildErrorResponse(e, HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND");
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    e.printStackTrace();
    return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR");
  }

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleException(DiscodeitException e){
    ErrorResponse errorResponse = ErrorResponse.builder()
        .status(e.getErrorCode().getHttpStatus().value())
        .errorCode(e.getErrorCode().getCode())
        .message(e.getMessage())
        .exceptionType(e.getClass().getSimpleName())
        .details(e.getDetails())
        .timestamp(Instant.now())
        .build();
    return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
    log.error("유효성 검증 실패: {}", e.getMessage());

    Map<String, Object> errors = e.getBindingResult()
        .getFieldErrors()
        .stream()
        .collect(Collectors.toMap(
            FieldError::getField,
            DefaultMessageSourceResolvable::getDefaultMessage,
            (existing, replacement) -> existing
        ));

    ErrorResponse errorResponse = ErrorResponse.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .errorCode("VALIDATION_FAILED")
        .message("유효성 검증에 실패했습니다.")
        .details(errors) // 상세 오류 포함
        .timestamp(Instant.now())
        .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  private ResponseEntity<ErrorResponse> buildErrorResponse(
      Exception e, HttpStatus status, String errorCode
  ) {
    log.error("예외 발생: {} - {}", e.getClass().getSimpleName(), e.getMessage(), e);
    ErrorResponse errorResponse = ErrorResponse.builder()
        .status(status.value())
        .errorCode(errorCode)
        .message(e.getMessage())
        .exceptionType(e.getClass().getSimpleName())
        .timestamp(Instant.now())
        .build();
    return ResponseEntity.status(status).body(errorResponse);
  }
}