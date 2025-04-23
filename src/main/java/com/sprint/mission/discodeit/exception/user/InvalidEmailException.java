package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class InvalidEmailException extends UserException {

  public InvalidEmailException() {
    super(ErrorCode.INVALID_EMAIL);
  }

  public static InvalidEmailException withEmail(String email) {
    InvalidEmailException exception = new InvalidEmailException();
    exception.addDetail("email", email);
    return exception;
  }
}
