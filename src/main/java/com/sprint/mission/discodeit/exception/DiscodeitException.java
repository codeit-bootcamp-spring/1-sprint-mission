package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

  private final ErrorCode errorCode;
  private final Instant timestamp;
  private final Map<String, Object> details = new HashMap<>();


  public DiscodeitException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.timestamp = Instant.now();
  }
}
