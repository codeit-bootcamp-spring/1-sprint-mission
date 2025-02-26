package com.sprint.mission.discodeit.exception.user;

import org.springframework.http.HttpStatus;

public class UserStatusAlreadyExistException extends UserException {

  public UserStatusAlreadyExistException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
