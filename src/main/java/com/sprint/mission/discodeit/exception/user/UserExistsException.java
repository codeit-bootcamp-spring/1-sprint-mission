package com.sprint.mission.discodeit.exception.user;

public class UserExistsException extends RuntimeException {

  public UserExistsException() {
    super();
  }

  public UserExistsException(String email) {
    super(String.format("User with email %s already exists", email));
  }
}
