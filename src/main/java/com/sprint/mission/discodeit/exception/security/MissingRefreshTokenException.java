package com.sprint.mission.discodeit.exception.security;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;

public class MissingRefreshTokenException extends SecurityException {

  public MissingRefreshTokenException(Map<String, Object> details) {
    super(Instant.now(), ErrorCode.MISSING_REFRESH_TOKEN, details);
  }
}
