package com.sprint.mission.discodeit.global.exception.userstatus;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import java.util.Map;

public class UserStatusAlreadyExistsException extends UserStatusException {

  public UserStatusAlreadyExistsException(ErrorCode errorCode) {
    super(errorCode);
  }

  public UserStatusAlreadyExistsException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
