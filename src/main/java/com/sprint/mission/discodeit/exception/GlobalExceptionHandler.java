package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.response.ErrorResponse;
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
    log.error("Discodeit Exception ", discodeitException);

    ErrorResponse errorResponse = ErrorResponse.of(discodeitException);

    return ResponseEntity
        .status(errorResponse.getStatus())
        .body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception exception) {
    log.error("Exception ", exception);

    ErrorResponse errorResponse = ErrorResponse.ofUnknown(exception);

    return ResponseEntity.
        status(errorResponse.getStatus())
        .body(errorResponse);
  }

}
