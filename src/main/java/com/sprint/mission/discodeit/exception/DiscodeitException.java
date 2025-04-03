package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;

public class DiscodeitException extends Exception {

  final Instant timestamp;
  final ErrorCode errorCode;
  final Map<String, Object> details;

  public DiscodeitException(Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
    this.timestamp = timestamp;
    this.errorCode = errorCode;
    this.details = details;
  }
}
