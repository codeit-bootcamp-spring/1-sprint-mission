package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserStatusNotFoundException extends UserStatusException {

  public UserStatusNotFoundException() {
    super(ErrorCode.USERSTATUS_NOT_FOUND);
  }

  public UserStatusNotFoundException(Map<String, Object> details) {
    super(ErrorCode.USERSTATUS_NOT_FOUND, details);
  }
}