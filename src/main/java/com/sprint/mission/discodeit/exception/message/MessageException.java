package com.sprint.mission.discodeit.exception.message;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class MessageException extends RuntimeException {

  @Getter
  private final HttpStatus status;

  public MessageException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }
}
