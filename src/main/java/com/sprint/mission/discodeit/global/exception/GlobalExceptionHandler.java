package com.sprint.mission.discodeit.global.exception;

import com.sprint.mission.discodeit.global.response.CustomApiResponse;
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
  @ExceptionHandler(RestApiException.class)
  public ResponseEntity<CustomApiResponse<ErrorResponse>> handleCustomException(
      RestApiException ex) {
    log.info(ex.getMessage());
    ErrorResponse errorResponse = ErrorResponse.builder()
        .errorCode(ex.getErrorCode())
        .detail(ex.getDetailMessage())
        .build();
    return handleExceptionInternal(errorResponse);
  }

  // 정의된 예외 이외에 모든 예외처리
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<CustomApiResponse<ErrorResponse>> handleAllException(Exception ex) {
    log.error(Arrays.toString(ex.getStackTrace()));
    ErrorResponse errorResponse = ErrorResponse.builder()
        .errorCode(ErrorCode.INTERNAL_SERVER_ERROR)
        .detail(ex.getMessage())
        .build();
    return handleExceptionInternal(errorResponse);
  }

  private ResponseEntity<CustomApiResponse<ErrorResponse>> handleExceptionInternal(
      ErrorResponse errorResponse) {
    return ResponseEntity
        .status(errorResponse.getHttpStatus())
        .body(CustomApiResponse.failure(errorResponse));
  }

}
