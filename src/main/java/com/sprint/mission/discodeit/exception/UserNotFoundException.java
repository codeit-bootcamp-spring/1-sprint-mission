package com.sprint.mission.discodeit.exception;

import java.time.Instant;

public class UserNotFoundException extends UserException {

  public UserNotFoundException() {
    super(Instant.now(), ErrorCode.USER_NOT_FOUND, "User not found");
  }
}