package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;

public class DiscodeitException extends RuntimeException {

  protected final Instant timestamp;
  protected final ErrorCode errorCode;
  protected final Map<String, Object> detail;

  // 생성자
  public DiscodeitException(ErrorCode errorCode, Map<String, Object> detail) {
    this.timestamp = Instant.now();
    this.errorCode = errorCode;
    this.detail = detail;
  }
}
