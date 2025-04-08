package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class InvalidCredentialsException extends AuthException {
  public InvalidCredentialsException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
