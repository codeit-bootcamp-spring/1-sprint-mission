package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserNotFoundException extends UserException {

  public UserNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }

  public static UserNotFoundException withUsername(String username) {
    UserNotFoundException exception = new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
    throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
  }

  //    InterestNotFoundException exception = new InterestNotFoundException();
  //    exception.addDetail("interestId", interestId);
  //    return exception;
}
