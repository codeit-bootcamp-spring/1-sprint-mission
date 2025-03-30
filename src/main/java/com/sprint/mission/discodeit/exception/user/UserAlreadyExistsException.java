package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class UserAlreadyExistsException extends UserException {

  private UserAlreadyExistsException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

  public static UserAlreadyExistsException of(Map<String, Object> details) {
    return new UserAlreadyExistsException(ErrorCode.USER_NOT_FOUND, details);
  }

  public static UserAlreadyExistsException of(UUID userId) {
    Map<String, Object> details = Map.of("User Id", userId);
    return new UserAlreadyExistsException(ErrorCode.USER_NOT_FOUND, details);
  }
}
