package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.response.ErrorResponse;
import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException ex) {
    HttpStatus status = resolveHttpStatus(ex.getErrorCode());

    ErrorResponse response = new ErrorResponse(
        Instant.now(),
        ex.getErrorCode().name(),
        ex.getErrorCode().getMessage(),
        ex.getDetails(),
        ex.getClass().getSimpleName(),
        status.value()
    );

    return ResponseEntity.status(status).body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
    ErrorResponse response = new ErrorResponse(
        Instant.now(),
        "UNEXPECTED_ERROR",
        ex.getMessage(),
        Map.of(),
        ex.getClass().getSimpleName(),
        HttpStatus.INTERNAL_SERVER_ERROR.value()
    );

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  private HttpStatus resolveHttpStatus(ErrorCode errorCode) {
    return switch (errorCode) {
      case USER_NOT_FOUND, CHANNEL_NOT_FOUND, FILE_NOT_FOUND, MESSAGE_NOT_FOUND,
           READ_STATUS_NOT_FOUND -> HttpStatus.NOT_FOUND;
      case USER_EMAIL_ALREADY_EXISTS, USER_USERNAME_ALREADY_EXISTS,
           PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED, READ_STATUS_ALREADY_EXISTS -> HttpStatus.BAD_REQUEST;
      case FILE_SAVE_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
      default -> HttpStatus.INTERNAL_SERVER_ERROR;
    };
  }
}
