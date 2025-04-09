package com.sprint.mission.discodeit.execption;

import com.sprint.mission.discodeit.common.util.TimeUtil;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException exception) {
    ErrorResponse errorResponse = new ErrorResponse(
        exception.getTimestamp(),
        exception.getErrorCode().getCode(),
        exception.getErrorCode().getMessage(),
        exception.getDetails(),
        exception.getClass().getSimpleName(),
        exception.getErrorCode().getStatus()
    );
    return ResponseEntity.status(HttpStatusCode.valueOf(exception.getErrorCode().getStatus()))
        .body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {
    String errorMessage = exception.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage()) // 필드명 + 메시지 조합
        .reduce((msg1, msg2) -> msg1 + ", " + msg2) // 여러 개일 경우 합치기
        .orElse("잘못된 요청입니다."); // 메시지가 없을 경우 기본값

    ErrorResponse errorResponse = new ErrorResponse(
        TimeUtil.getCurrentTime(),
        "Bean Validation",
        errorMessage,
        null,
        exception.getClass().getSimpleName(),
        400
    );
    return ResponseEntity.status(HttpStatusCode.valueOf(errorResponse.getStatus()))
        .body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handException(
      Exception exception) {
    ErrorResponse errorResponse = new ErrorResponse(
        TimeUtil.getCurrentTime(),
        "server error",
        exception.getMessage(),
        null,
        exception.getClass().getSimpleName(),
        400
    );
    return ResponseEntity.status(HttpStatusCode.valueOf(errorResponse.getStatus()))
        .body(errorResponse);
  }
}
