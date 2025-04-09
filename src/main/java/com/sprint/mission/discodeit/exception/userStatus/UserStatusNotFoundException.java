package com.sprint.mission.discodeit.exception.userStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class UserStatusNotFoundException extends UserStatusException {

  public UserStatusNotFoundException(Map<String, Object> details) {
    super(ErrorCode.USERSTATUS_NOT_FOUND, details);
  }

  public static UserStatusNotFoundException byId(UUID id) {
    return new UserStatusNotFoundException(Map.of("id", id));
  }

  public static UserStatusNotFoundException byUserId(UUID userId) {
    return new UserStatusNotFoundException(Map.of("userId", userId));
  }
}
