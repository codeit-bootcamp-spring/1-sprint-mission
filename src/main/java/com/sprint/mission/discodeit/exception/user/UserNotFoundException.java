package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class UserNotFoundException extends UserException {

  public UserNotFoundException(Map<String, Object> details) {
    super(ErrorCode.USER_NOT_FOUND, details);
  }

  public static UserNotFoundException byId(UUID id) {
    return new UserNotFoundException(Map.of("id", id));
  }

  public static UserNotFoundException byName(String username) {
    return new UserNotFoundException(Map.of("username", username));
  }
}
