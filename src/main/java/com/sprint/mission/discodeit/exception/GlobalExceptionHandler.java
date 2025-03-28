package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.response.ErrorResponse;
import java.time.Instant;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  protected ResponseEntity<ErrorResponse> handleDiscodeitException(
      DiscodeitException discodeitException
  ) {
    ErrorCode errorCode = discodeitException.getErrorCode();
    ErrorResponse errorResponse = new ErrorResponse(
        discodeitException.getTimestamp(),
        errorCode.getMessage(),
        errorCode.getCode(),
        discodeitException.getDetails(),
        "exceptionType",
        errorCode.getStatus().value()
    );
    return ResponseEntity
        .status(errorCode.getStatus())
        .body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException2(Exception exception) {
    exception.printStackTrace();
    log.error("Exception ", exception);
    ErrorResponse errorResponse = new ErrorResponse(
        Instant.now(),
        exception.getMessage(),
        "code",
        Map.of(),
        "exceptionType",
        HttpStatus.INTERNAL_SERVER_ERROR.value()
    );
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

}
