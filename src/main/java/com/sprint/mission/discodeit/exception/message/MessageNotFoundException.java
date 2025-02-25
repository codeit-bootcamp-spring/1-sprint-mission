package com.sprint.mission.discodeit.exception.message;

import org.springframework.http.HttpStatus;

public class MessageNotFoundException extends MessageException {

  public MessageNotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND);
  }
}
