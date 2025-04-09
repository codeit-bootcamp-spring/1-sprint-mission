package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.error.ErrorCode;
import java.util.Map;

public class UserAlreadyExistsException extends UserException {

  public UserAlreadyExistsException(ErrorCode errorCode) {
    super(errorCode);
  }

  public UserAlreadyExistsException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
