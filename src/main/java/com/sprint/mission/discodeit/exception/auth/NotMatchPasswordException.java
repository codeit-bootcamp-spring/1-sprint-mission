package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class NotMatchPasswordException extends AuthException {

  public NotMatchPasswordException() {
    super(ErrorCode.NOT_MATCH_PASSWORD);
  }
}
