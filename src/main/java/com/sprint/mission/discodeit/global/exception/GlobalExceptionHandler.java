package com.sprint.mission.discodeit.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// Controller + ResponseBody
@RestControllerAdvice(basePackages = {"com.sprint.mission.discodeit.controller.api"})
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  // RestApiException 에 대한 예외 처리하기
  @ExceptionHandler(RestApiException.class)
  public ResponseEntity<Object> handleCustomException(RestApiException ex) {
    ErrorResponse errorResponse = ErrorResponse.builder()
        .errorCode(ex.getErrorCode())
        .detail(ex.getDetailMessage())
        .build();
    return handleExceptionInternal(errorResponse);
  }

//  @ExceptionHandler(Exception.class)
//  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//  protected ResponseEntity<Object> handleAllException(Exception ex) {
//    ErrorResponse errorResponse = ErrorResponse.builder()
//        .errorCode(ErrorCode.INTERNAL_SERVER_ERROR)
//        .detail(ex.getMessage())
//        .build();
//    return handleExceptionInternal(errorResponse);
//  }

  private ResponseEntity<Object> handleExceptionInternal(ErrorResponse errorResponse) {
    return ResponseEntity
        .status(errorResponse.getStatus())
        .body(errorResponse);
  }

}
