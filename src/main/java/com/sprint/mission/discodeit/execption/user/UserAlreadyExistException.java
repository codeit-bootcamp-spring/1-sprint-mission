package com.sprint.mission.discodeit.execption.user;

import com.sprint.mission.discodeit.execption.ErrorCode;
import java.util.Map;

public class UserAlreadyExistException extends UserException {

  public UserAlreadyExistException() {
    super(ErrorCode.USER_ALREADY_EXIST);
  }

  public UserAlreadyExistException(Map<String, Object> details) {
    super(ErrorCode.USER_ALREADY_EXIST, details);
  }
}
