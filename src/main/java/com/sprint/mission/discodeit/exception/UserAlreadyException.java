package com.sprint.mission.discodeit.exception;

import java.time.Instant;

public class UserAlreadyException extends UserException {

  public UserAlreadyException() {
    super(Instant.now(), ErrorCode.DUPLICATE_USER, "User already exists");
  }
}