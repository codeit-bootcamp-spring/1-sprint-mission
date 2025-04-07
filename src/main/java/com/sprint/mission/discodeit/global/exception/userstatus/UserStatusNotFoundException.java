package com.sprint.mission.discodeit.global.exception.userstatus;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import java.util.Map;

public class UserStatusNotFoundException extends UserStatusException {

  public UserStatusNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }

  public UserStatusNotFoundException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
