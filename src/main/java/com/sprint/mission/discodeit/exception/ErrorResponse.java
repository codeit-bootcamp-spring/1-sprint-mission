package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

  private Instant timestamp;
  private String code;
  private String message;
  private Map<String, Object> details;
  private String exceptionType;
  private int status;

  public ErrorResponse(DiscodeitException e, int status) {
    this.timestamp = Instant.now();
    this.code = e.getErrorCode().toString();
    this.message = e.getMessage();
    this.details = e.getDetails();
    this.status = status;
  }
}
