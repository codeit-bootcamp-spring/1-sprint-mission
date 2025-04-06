package com.sprint.mission.discodeit.exception;

import java.time.Instant;

public class UserException extends DiscodeitException {

  public UserException(Instant timestamp, ErrorCode errorCode, String details) {
    super(timestamp, errorCode, details);
  }
}