package com.sprint.mission.discodeit.global.exception.auth;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import java.util.Map;

public class AuthenticationFailedException extends AuthException {

  public AuthenticationFailedException(ErrorCode errorCode) {
    super(errorCode);
  }

  public AuthenticationFailedException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
