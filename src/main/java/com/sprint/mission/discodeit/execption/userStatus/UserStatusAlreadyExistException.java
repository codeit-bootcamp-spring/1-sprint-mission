package com.sprint.mission.discodeit.execption.userStatus;

import com.sprint.mission.discodeit.execption.ErrorCode;
import java.util.Map;

public class UserStatusAlreadyExistException extends UserStatusException {

  public UserStatusAlreadyExistException() {
    super(ErrorCode.USERSTATUS_ALREADY_EXIST);
  }

  public UserStatusAlreadyExistException(Map<String, Object> details) {
    super(ErrorCode.USERSTATUS_ALREADY_EXIST, details);
  }
}
