package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class DiscodeitException extends RuntimeException {
  private final ErrorCode errorCode;
  private final Map<String, Object> details;

  public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode.getMessage(), null, false, false);
    this.errorCode = errorCode;
    this.details = details;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  public Map<String, Object> getDetails() {
    return details;
  }
}
