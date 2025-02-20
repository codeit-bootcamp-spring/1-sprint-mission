package com.sprint.mission.discodeit.exception;

public class NotFoundException extends IllegalArgumentException {
  public NotFoundException(String message) {
    super(message);
  }

  public NotFoundException() {
    super();
  }
}
