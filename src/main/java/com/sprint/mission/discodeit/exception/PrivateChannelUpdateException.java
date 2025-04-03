package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;

public class PrivateChannelUpdateException extends ChannelException {

  public PrivateChannelUpdateException(Instant timestamp, ErrorCode errorCode,
      Map<String, Object> details) {
    super(timestamp, errorCode, details);
  }
}
