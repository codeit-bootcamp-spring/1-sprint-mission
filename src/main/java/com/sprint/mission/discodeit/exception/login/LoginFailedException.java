package com.sprint.mission.discodeit.exception.login;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class LoginFailedException extends DiscodeitException {

  public LoginFailedException(Map<String, Object> details) {
    super(ErrorCode.LOGIN_FAILED, details);
  }

}