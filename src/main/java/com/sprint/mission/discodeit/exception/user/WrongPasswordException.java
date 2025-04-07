package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class WrongPasswordException extends UserException {

  public WrongPasswordException() {
    super(ErrorCode.WRONG_PASSWORD);
  }
}
