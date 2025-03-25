package com.sprint.mission.discodeit.exception;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

    log.error("유효성 검사 실패 : {}", ex.getMessage());

    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST)
        .message("유효성 검증에 실패하셨습니다.")
        .details(
            ex.getObjectName() + " : " + ex.getBindingResult().getFieldError().getDefaultMessage())
        .build();

    return handleExceptionInternal(errorResponse);
  }

  @ExceptionHandler(RestApiException.class)
  public ResponseEntity<Object> handleCustomException(
      RestApiException ex) {

    log.error("에러 코드 : {}, 에러 발생 : {} ({})", ex.getErrorCode(), ex.getMessage(), ex.getDetails());

    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(ex.getErrorCode().getHttpStatus())
        .message(ex.getErrorCode().getMessage())
        .details(ex.getDetails())
        .build();
    return handleExceptionInternal(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleBadRequestException(
      Exception ex) {

    log.error("서버 에러 발생 : {}", ex.getMessage());

    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(GlobalErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
        .message(GlobalErrorCode.INTERNAL_SERVER_ERROR.getMessage())
        .build();
    return handleExceptionInternal(errorResponse);
  }

  private ResponseEntity<Object> handleExceptionInternal(
      ErrorResponse errorResponse) {
    return ResponseEntity
        .status(errorResponse.getStatus())
        .body(errorResponse);
  }
}
