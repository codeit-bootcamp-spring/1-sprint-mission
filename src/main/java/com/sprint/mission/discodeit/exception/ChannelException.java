package com.sprint.mission.discodeit.exception;

import java.time.Instant;

public class ChannelException extends DiscodeitException {

  public ChannelException(Instant timestamp, ErrorCode errorCode, String details) {
    super(timestamp, errorCode, details);
  }
}