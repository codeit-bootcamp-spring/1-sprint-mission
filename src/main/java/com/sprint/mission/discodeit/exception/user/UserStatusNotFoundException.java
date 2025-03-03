package com.sprint.mission.discodeit.exception.user;

import org.springframework.http.HttpStatus;

public class UserStatusNotFoundException extends UserException {

  public UserStatusNotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND);
  }
}
