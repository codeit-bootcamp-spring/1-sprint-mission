package com.sprint.mission.discodeit.exception.userStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserStatusNotFoundException extends UserStatusException {
  public UserStatusNotFoundException(Map<String, Object> details) {
    super(ErrorCode.USER_STATUS_NOT_FOUND, details);
  }
}
