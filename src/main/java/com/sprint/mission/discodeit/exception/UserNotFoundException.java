package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class UserNotFoundException extends UserException {


  public UserNotFoundException(Map<String, Object> details) {
    super(ErrorCode.USER_NOT_FOUND, details);
  }
}
