package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
    Instant timestamp,
    String code,
    String message,
    Map<String, Object> details,
    String exceptionType,
    int status
) {

  public static ErrorResponse fromException(Exception e, int status) {
    return new ErrorResponse(
        Instant.now(),
        e.getClass().getSimpleName(),
        e.getMessage(),
        Map.of(),
        e.getClass().getSimpleName(),
        status
    );
  }

  public static ErrorResponse fromDiscodeitException(DiscodeitException e) {
    return new ErrorResponse(
        e.getTimestamp(),
        e.getErrorCode().name(),
        e.getErrorCode().getMessage(),
        e.getDetails(),
        e.getClass().getSimpleName(),
        e.getErrorCode().getStatus()
    );
  }
}
