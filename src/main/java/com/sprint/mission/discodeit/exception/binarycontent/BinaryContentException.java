package com.sprint.mission.discodeit.exception.binarycontent;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class BinaryContentException extends RuntimeException {

  @Getter
  private final HttpStatus status;

  public BinaryContentException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }
}
