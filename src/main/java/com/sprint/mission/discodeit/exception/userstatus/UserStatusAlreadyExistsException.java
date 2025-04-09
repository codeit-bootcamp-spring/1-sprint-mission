package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class UserStatusAlreadyExistsException extends UserStatusException {

  private UserStatusAlreadyExistsException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

  public static UserStatusAlreadyExistsException of(UUID userId, UUID userStatusId) {
    Map<String, Object> details = Map.of("User Id", userId, "User Status Id", userStatusId);
    return new UserStatusAlreadyExistsException(ErrorCode.DUPLICATE_USER_STATUS, details);
  }
}
