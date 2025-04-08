package com.sprint.mission.discodeit.exception.userStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserStatusNotFoundException extends UserStatusException {

  public UserStatusNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
