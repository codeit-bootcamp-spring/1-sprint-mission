package com.sprint.mission.discodeit.exception;

public class InvalidRequestException extends RuntimeException {

  public InvalidRequestException() {
    super("A required parameter is Invalid.");
  }


  public InvalidRequestException(String message) {
    super(message);
  }
}
