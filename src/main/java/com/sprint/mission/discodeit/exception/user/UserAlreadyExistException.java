package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserAlreadyExistException extends UserException {

  public UserAlreadyExistException(ErrorCode errorCode) {
    super(errorCode);
  }
}
