package com.sprint.mission.discodeit.error.exception.user;

import com.sprint.mission.discodeit.error.ErrorCode;

public class UserAlreadyExistException extends UserException {

  public UserAlreadyExistException() {
    super(ErrorCode.DUPLICATE_USER);
  }
}
