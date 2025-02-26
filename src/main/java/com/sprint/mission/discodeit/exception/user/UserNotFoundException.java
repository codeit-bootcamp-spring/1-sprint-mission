package com.sprint.mission.discodeit.exception.user;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException() {
    super();
  }

  public UserNotFoundException(String username) {
    super(String.format("User with id %s not found", username));
  }
}
