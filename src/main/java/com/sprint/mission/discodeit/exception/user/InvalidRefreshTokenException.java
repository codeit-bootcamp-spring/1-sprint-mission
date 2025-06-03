package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class InvalidRefreshTokenException extends UserException {
  public InvalidRefreshTokenException() {
    super(ErrorCode.INVALID_REFRESH_TOKEN_CREDENTIALS);
  }

  public static InvalidRefreshTokenException expiredOrInvalid() {
    InvalidRefreshTokenException exception = new InvalidRefreshTokenException();
    return exception;
  }
}
