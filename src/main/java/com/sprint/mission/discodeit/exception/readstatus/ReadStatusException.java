package com.sprint.mission.discodeit.exception.readstatus;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ReadStatusException extends RuntimeException {

  @Getter
  private final HttpStatus status;

  public ReadStatusException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }
}
