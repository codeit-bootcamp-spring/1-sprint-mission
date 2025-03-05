package com.sprint.mission.discodeit.exception.userstatus;

import java.util.UUID;

public class UserStatusNotFoundException extends RuntimeException {

  public UserStatusNotFoundException() {
    super();
  }

  public UserStatusNotFoundException(UUID userId) {
    super(String.format("UserStatus with userId %s not found", userId));
  }
}
