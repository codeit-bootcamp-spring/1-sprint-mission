package com.sprint.mission.discodeit.exception;

public class ValidationException extends IllegalArgumentException {
  public ValidationException(String message) {
    super(message);
  }

  public ValidationException() {
    super();
  }
}
