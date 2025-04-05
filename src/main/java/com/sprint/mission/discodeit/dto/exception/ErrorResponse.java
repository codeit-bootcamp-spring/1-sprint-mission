package com.sprint.mission.discodeit.dto.exception;

import com.sprint.mission.discodeit.exception.DiscodeitException;
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
  public static ErrorResponse from(DiscodeitException e) {
    return new ErrorResponse(
        Instant.now(),
        e.getErrorCode().getCode(),
        e.getErrorCode().getMessage(),
        e.getDetails(),
        e.getClass().getSimpleName(),
        e.getErrorCode().getHttpStatus().value()
    );
  }
}
