package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UsernameAlreadyExistsException extends UserException {

  public UsernameAlreadyExistsException() {
    super(ErrorCode.DUPLICATE_USERNAME);
  }

  public UsernameAlreadyExistsException(Map<String, Object> details) {
    super(ErrorCode.DUPLICATE_USERNAME, details);
  }

}
