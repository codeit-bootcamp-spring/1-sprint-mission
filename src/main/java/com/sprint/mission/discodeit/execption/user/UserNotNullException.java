package com.sprint.mission.discodeit.execption.user;

import com.sprint.mission.discodeit.execption.ErrorCode;
import java.util.Map;

public class UserNotNullException extends UserException {

  public UserNotNullException() {
    super(ErrorCode.USER_NOT_NULL);
  }

  public UserNotNullException(Map<String, Object> details) {
    super(ErrorCode.USER_NOT_NULL, details);
  }
}
