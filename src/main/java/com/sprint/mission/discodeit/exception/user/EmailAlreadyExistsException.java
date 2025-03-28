package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class EmailAlreadyExistsException extends UserException {

  public EmailAlreadyExistsException(Map<String, Object> details) {
    super(ErrorCode.DUPLICATE_EMAIL, details);
  }
}