package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class EmailAlreadyExistsException extends DiscodeitException {

  public EmailAlreadyExistsException(String email) {
    super(ErrorCode.USER_ALREADY_EXISTS, "User with email " + email + " already exists");
  }
}