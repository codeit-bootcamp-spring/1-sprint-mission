package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class LoginFailedException extends DiscodeitException {

  public LoginFailedException(Map<String, Object> details) {
    super(ErrorCode.LOGIN_FAILED, details);
  }

}