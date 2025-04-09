package com.sprint.mission.discodeit.execption.user;

import com.sprint.mission.discodeit.execption.ErrorCode;
import java.util.Map;

public class UserNotFoundException extends UserException {

  public UserNotFoundException() {
    super(ErrorCode.USER_NOT_FOUND);
  }

  public UserNotFoundException(Map<String, Object> details) {
    super(ErrorCode.USER_NOT_FOUND, details);
  }
}
