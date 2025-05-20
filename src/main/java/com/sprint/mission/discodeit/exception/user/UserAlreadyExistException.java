package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserAlreadyExistException extends UserException {

  public UserAlreadyExistException() {
    super(ErrorCode.DUPLICATE_USER);
  }

  public static UserAlreadyExistException withEmail(String email) {
    UserAlreadyExistException exception = new UserAlreadyExistException();
    exception.addDetail("email", email);
    return exception;
  }

  public static UserAlreadyExistException withUsername(String username) {
    UserAlreadyExistException exception = new UserAlreadyExistException();
    exception.addDetail("username", username);
    return exception;
  }
}
