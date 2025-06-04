package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

  private Instant timestamp;
  private String code;
  private String message;
  private Map<String, Object> details;
  private String exceptionType;
  private int status;

  public ErrorResponse(DiscodeitException exception, int status) {
    this(Instant.now(), exception.getErrorCode().name(), exception.getMessage(),
        exception.getDetails(), exception.getClass().getSimpleName(), status);
  }

  public ErrorResponse(Exception exception, int status) {
    this(Instant.now(), exception.getClass().getSimpleName(), exception.getMessage(),
        new HashMap<>(), exception.getClass().getSimpleName(), status);
  }
}
