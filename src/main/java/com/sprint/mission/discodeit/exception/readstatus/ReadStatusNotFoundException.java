package com.sprint.mission.discodeit.exception.readstatus;

import org.springframework.http.HttpStatus;

public class ReadStatusNotFoundException extends ReadStatusException {

  public ReadStatusNotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND);
  }
}
