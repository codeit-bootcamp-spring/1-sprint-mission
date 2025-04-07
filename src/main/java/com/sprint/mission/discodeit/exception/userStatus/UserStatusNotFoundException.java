package com.sprint.mission.discodeit.exception.userStatus;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class UserStatusNotFoundException extends DiscodeitException {

  public UserStatusNotFoundException(UUID userStatusId) {
    super(ErrorCode.USER_STATUS_NOT_FOUND, "UserStatus with id " + userStatusId + " not found");
  }
}