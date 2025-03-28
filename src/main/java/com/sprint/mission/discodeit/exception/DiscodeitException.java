package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

public class DiscodeitException extends RuntimeException {

  private final Instant timestamp;
  private final ErrorCode errorCode;
  private final Map<String, Object> details;

  public DiscodeitException(ErrorCode errorCode) {
    this(errorCode, Collections.emptyMap());
  }

  public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode.getMessage());
    this.timestamp = Instant.now();
    this.errorCode = errorCode;
    this.details = details != null ? details : Collections.emptyMap();
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  public Map<String, Object> getDetails() {
    return details;
  }
}