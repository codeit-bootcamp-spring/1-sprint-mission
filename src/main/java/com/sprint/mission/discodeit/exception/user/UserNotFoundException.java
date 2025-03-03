package com.sprint.mission.discodeit.exception.user;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends UserException {

  public UserNotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND);
  }
}
