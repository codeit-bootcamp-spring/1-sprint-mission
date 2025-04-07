package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserAlreadyExistsException extends DiscodeitException {

  public UserAlreadyExistsException(String username) {
    super(ErrorCode.USER_ALREADY_EXISTS, "User with username " + username + " already exists");
  }

  public UserAlreadyExistsException(String email, boolean isEmail) {
    super(ErrorCode.USER_ALREADY_EXISTS, "User with email " + email + " already exists");
  }
}