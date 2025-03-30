package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;

public class UsernameAlreadyExistsException extends UserException {

  public UsernameAlreadyExistsException(Map<String, Object> details) {
    super(Instant.now(), ErrorCode.USERNAME_ALREADY_EXISTS, details);
  }
}
