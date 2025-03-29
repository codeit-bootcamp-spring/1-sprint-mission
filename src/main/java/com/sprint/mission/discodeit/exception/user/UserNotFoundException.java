package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserNotFoundException extends UserException {

  private UserNotFoundException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

  public static UserNotFoundException of(Map<String, Object> details) {
    return new UserNotFoundException(ErrorCode.USER_NOT_FOUND, details);
  }
}
