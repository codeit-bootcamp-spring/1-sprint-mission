package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import java.util.Map;

@Getter
public class DiscodeitException extends RuntimeException {
  private final ErrorCode errorCode;
  private final Map<String, Object> details;

  public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.details = details;
  }
}