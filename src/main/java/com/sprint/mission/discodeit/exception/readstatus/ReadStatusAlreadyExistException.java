package com.sprint.mission.discodeit.exception.readstatus;

import org.springframework.http.HttpStatus;

public class ReadStatusAlreadyExistException extends ReadStatusException {

  public ReadStatusAlreadyExistException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
