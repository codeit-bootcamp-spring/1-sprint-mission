package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class InvalidRefreshTokenException extends TokenException {

  public InvalidRefreshTokenException(ErrorCode errorCode) {
    super(errorCode);
  }

  public InvalidRefreshTokenException(ErrorCode errorCode, Throwable cause) {
    super(errorCode, cause);
  }
}
