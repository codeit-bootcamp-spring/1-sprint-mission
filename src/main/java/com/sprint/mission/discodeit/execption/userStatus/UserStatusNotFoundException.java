package com.sprint.mission.discodeit.execption.userStatus;

import com.sprint.mission.discodeit.execption.ErrorCode;
import java.util.Map;

public class UserStatusNotFoundException extends UserStatusException {

  public UserStatusNotFoundException() {
    super(ErrorCode.USERSTATUS_NOT_FOUND);
  }

  public UserStatusNotFoundException(Map<String, Object> details) {
    super(ErrorCode.USERSTATUS_NOT_FOUND, details);
  }
}
