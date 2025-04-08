package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorResponse {

  private final Instant timestamp;
  private final String code;
  private final String message;
  private final Map<String, Object> details;
  private final String exceptionType;
  private final int status;

  public ErrorResponse(HttpStatus status, ErrorCode errorCode) {
    this.timestamp = Instant.now();
    this.code = status.getReasonPhrase();
    this.message = errorCode.getMessage();
    this.details = null;
    this.exceptionType = errorCode.name();
    this.status = status.value();

  }

  public ErrorResponse(HttpStatus status, ErrorCode errorCode, Map<String, Object> details) {
    this.timestamp = Instant.now();
    this.code = status.getReasonPhrase();
    this.message = errorCode.getMessage();
    this.details = details;
    this.exceptionType = errorCode.name();
    this.status = status.value();
  }

}
