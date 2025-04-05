package com.sprint.mission.discodeit.exception.userStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserStatusAlreadyExistsException extends UserStatusException {
  public UserStatusAlreadyExistsException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
