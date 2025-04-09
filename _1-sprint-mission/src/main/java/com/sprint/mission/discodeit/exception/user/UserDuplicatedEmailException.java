package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserDuplicatedEmailException extends UserException {

  public UserDuplicatedEmailException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
