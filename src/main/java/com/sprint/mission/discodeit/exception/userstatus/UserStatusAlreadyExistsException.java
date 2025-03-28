package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserStatusAlreadyExistsException extends UserStatusException {

  public UserStatusAlreadyExistsException(Map<String, Object> details) {
    super(ErrorCode.USER_STATUS_ALREADY_EXISTS, details);
  }
}
