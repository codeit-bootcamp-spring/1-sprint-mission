package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class DuplicatedUsernameException extends UserException {

  public DuplicatedUsernameException() {
    super(ErrorCode.DUPLICATED_USERNAME);
  }
}
