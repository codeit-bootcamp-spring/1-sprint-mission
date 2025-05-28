package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

  private ErrorCode errorCode;
  private Instant timestamp;
  private Map<String, Object> details;


  public DiscodeitException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.timestamp = Instant.now();
    this.errorCode = errorCode;
    this.details = new HashMap<>();
  }

  public DiscodeitException(ErrorCode errorCode, Throwable cause) {
    super(errorCode.getMessage(), cause);
    this.timestamp = Instant.now();
    this.errorCode = errorCode;
    this.details = new HashMap<>();
  }

  public void addDetail(String key, Object value) {
    this.details.put(key, value);
  }
}
