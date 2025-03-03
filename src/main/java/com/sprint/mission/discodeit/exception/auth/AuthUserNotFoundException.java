package com.sprint.mission.discodeit.exception.auth;

import org.springframework.http.HttpStatus;

public class AuthUserNotFoundException extends AuthException {

  public AuthUserNotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND);
  }
}
