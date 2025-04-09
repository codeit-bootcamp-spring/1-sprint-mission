package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserNotFoundExeption extends UserException {

  public UserNotFoundExeption(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
