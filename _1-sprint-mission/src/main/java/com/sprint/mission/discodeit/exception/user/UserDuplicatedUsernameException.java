package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserDuplicatedUsernameException extends UserException {

  public UserDuplicatedUsernameException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
