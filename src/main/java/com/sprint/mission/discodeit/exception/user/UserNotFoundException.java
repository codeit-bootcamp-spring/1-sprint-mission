package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Collections;
import java.util.UUID;

public class UserNotFoundException extends UserException {

  public UserNotFoundException(UUID userId) {
    super(ErrorCode.USER_NOT_FOUND, Collections.singletonMap("userId", userId));
  }

  public UserNotFoundException(String username) {
    super(ErrorCode.USER_NOT_FOUND, Collections.singletonMap("username", username));
  }
}
