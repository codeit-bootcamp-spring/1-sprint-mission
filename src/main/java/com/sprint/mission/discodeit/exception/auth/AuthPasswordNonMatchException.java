package com.sprint.mission.discodeit.exception.auth;

import org.springframework.http.HttpStatus;

public class AuthPasswordNonMatchException extends AuthException {

  public AuthPasswordNonMatchException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
