package com.sprint.mission.discodeit.exception.security;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;

public class UserUnauthorizedException extends SecurityException {

  public UserUnauthorizedException(Map<String, Object> details) {
    super(Instant.now(), ErrorCode.USER_UNAUTHORIZED, details);
  }
}
