package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class InvalidUserCredentialsException extends DiscodeitException {

  public InvalidUserCredentialsException() {
    super(ErrorCode.INVALID_USER_CREDENTIALS);
  }
}