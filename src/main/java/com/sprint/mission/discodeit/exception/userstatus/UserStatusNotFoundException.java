package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserStatusNotFoundException extends UserStatusException {

  private UserStatusNotFoundException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

  public static UserStatusNotFoundException of(Map<String, Object> details) {
    return new UserStatusNotFoundException(ErrorCode.USER_STATUS_NOT_FOUND, details);
  }
}
