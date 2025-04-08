package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class AuthNotFoundException extends AuthException {

  public AuthNotFoundException(Map<String, Object> details) {
    super(ErrorCode.USERNAME_NOT_FOUND, details);;
  }
}
