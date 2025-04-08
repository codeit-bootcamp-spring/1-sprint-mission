package com.sprint.mission.discodeit.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionAdvice {

//  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//  @ExceptionHandler(Exception.class)
//  public ErrorResponse exception(Exception e) {
//
//    return ErrorResponse.builder()
//        .timestamp(Instant.now())
//        .code(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
//        .message(e.getMessage())
//        .details(Map.of(e.getClass().getSimpleName(), "알 수 없는 서버 오류가 발생했습니다."))
//        .exceptionType(e.getClass().getSimpleName())
//        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
//        .build();
//  }
}
