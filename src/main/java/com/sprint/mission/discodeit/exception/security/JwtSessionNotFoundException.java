package com.sprint.mission.discodeit.exception.security;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;

public class JwtSessionNotFoundException extends SecurityException {

  public JwtSessionNotFoundException(Map<String, Object> details) {
    super(Instant.now(), ErrorCode.JWT_SESSION_NOT_FOUND, details);
  }
}
