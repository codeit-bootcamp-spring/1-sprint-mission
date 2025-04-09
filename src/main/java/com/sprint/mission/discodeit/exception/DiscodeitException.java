package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.error.ErrorCode;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

  private final ErrorCode errorCode;
  private final Instant timestamp;
  private final Map<String, Object> details;

  public DiscodeitException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.timestamp = Instant.now();
    details = new HashMap<>();
  }

  public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.timestamp = Instant.now();
    this.details = details;
  }

  public int getStatusCode() {
    return errorCode.getStatus().value();
  }
}
