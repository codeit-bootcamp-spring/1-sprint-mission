package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class InvalidRefreshTokenException extends AuthException {

  public InvalidRefreshTokenException() {
    super(ErrorCode.MISSING_OR_INVALID_REFRESH_TOKEN);
  }
}
