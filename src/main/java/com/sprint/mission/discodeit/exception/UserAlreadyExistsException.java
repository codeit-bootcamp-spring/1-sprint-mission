package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class UserAlreadyExistsException extends UserException {

  public UserAlreadyExistsException(Map<String, Object> details) {
    super(ErrorCode.DUPLICATE_USER, details);
  }
}
