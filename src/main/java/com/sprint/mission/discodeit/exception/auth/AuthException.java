package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class AuthException extends DiscodeitException {
  public AuthException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
