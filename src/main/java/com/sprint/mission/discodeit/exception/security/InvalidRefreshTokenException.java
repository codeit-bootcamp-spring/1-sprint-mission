package com.sprint.mission.discodeit.exception.security;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;

public class InvalidRefreshTokenException extends SecurityException {

  public InvalidRefreshTokenException(Map<String, Object> details) {
    super(Instant.now(), ErrorCode.INVALID_REFRESH_TOKEN, details);
  }
}
