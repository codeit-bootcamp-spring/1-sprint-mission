package com.sprint.mission.discodeit.exception.user;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistException extends UserException {

  public UserAlreadyExistException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
