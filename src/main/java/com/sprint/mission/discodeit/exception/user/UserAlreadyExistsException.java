package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserAlreadyExistsException extends UserException {

  private UserAlreadyExistsException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

  public static UserAlreadyExistsException email(String email) {
    return new UserAlreadyExistsException(ErrorCode.USER_EMAIL_ALREADY_EXISTS,
        Map.of("email", email));
  }

  public static UserAlreadyExistsException username(String username) {
    return new UserAlreadyExistsException(ErrorCode.USER_USERNAME_ALREADY_EXISTS,
        Map.of("username", username));
  }
}
