package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserAlreadyExistsException extends UserException {

  public UserAlreadyExistsException(Map<String, Object> details) {
    super(ErrorCode.DUPLICATE_USER, details);
  }

  public UserAlreadyExistsException(String message, Map<String, Object> details) {
    super(ErrorCode.DUPLICATE_USER, message, details);
  }
}
