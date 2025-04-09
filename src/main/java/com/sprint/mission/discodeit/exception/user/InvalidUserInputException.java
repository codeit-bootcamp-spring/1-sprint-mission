package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class InvalidUserInputException extends UserException {

  public InvalidUserInputException() {
    super(ErrorCode.INVALID_USER_INPUT);
  }
}
