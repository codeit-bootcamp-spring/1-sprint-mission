package com.sprint.mission.discodeit.exception.user;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class UserException extends RuntimeException {

  @Getter
  private final HttpStatus status;

  public UserException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }
}
