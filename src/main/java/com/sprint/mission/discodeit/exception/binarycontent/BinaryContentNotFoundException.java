package com.sprint.mission.discodeit.exception.binarycontent;

import org.springframework.http.HttpStatus;

public class BinaryContentNotFoundException extends BinaryContentException {

  public BinaryContentNotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND);
  }
}
