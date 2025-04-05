package com.sprint.mission.discodeit.global.exception;

import com.sprint.mission.discodeit.global.response.CustomApiResponse;
import java.time.Instant;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// Controller + ResponseBody
@Slf4j
@RestControllerAdvice(basePackages = {"com.sprint.mission.discodeit.controller.api"})
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  // RestApiException 에 대한 예외 처리하기
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(
      BusinessException ex) {
    log.info("{} - code:{}", ex.getClass().getSimpleName(), ex.getErrorCode().getCode());

    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(ex.getTimestamp())
        .code(ex.getErrorCode().getCode())
        .message(ex.getErrorCode().getMessage())
        .details(ex.getDetails())
        .exceptionType(ex.getClass().getSimpleName())
        .status(ex.getErrorCode().getHttpStatus().value())
        .build();

    return handleExceptionInternal(errorResponse);
  }

  // 정의된 예외 이외에 모든 예외처리
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleAllException(Exception ex) {

    log.error(Arrays.toString(ex.getStackTrace()));

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
