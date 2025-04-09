package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserStatusAlreadyExistException extends UserStatusException {

  public UserStatusAlreadyExistException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
