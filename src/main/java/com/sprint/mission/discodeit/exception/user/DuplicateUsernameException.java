package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class DuplicateUsernameException extends UserException {

  public DuplicateUsernameException(String username) {
    super(ErrorCode.DUPLICATE_USERNAME, Map.of("username", username));
  }
}
