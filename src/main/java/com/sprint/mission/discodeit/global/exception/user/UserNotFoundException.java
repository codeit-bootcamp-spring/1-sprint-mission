package com.sprint.mission.discodeit.global.exception.user;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import java.util.Map;

public class UserNotFoundException extends UserException {

  public UserNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }

  public UserNotFoundException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
