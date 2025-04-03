package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;

public class UserException extends DiscodeitException {

  public UserException(Instant timestamp, ErrorCode errorCode,
      Map<String, Object> details) {
    super(timestamp, errorCode, details);
  }
}
