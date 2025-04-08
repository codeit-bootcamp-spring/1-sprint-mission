package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

  public DiscodeitException(Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
    this.timestamp = timestamp;
    this.errorCode = errorCode;
    this.details = details;
  }

  private final Instant timestamp;
  private final ErrorCode errorCode;
  private final Map<String, Object> details;

}
