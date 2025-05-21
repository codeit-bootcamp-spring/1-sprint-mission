package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class InvalidLoginRequestException extends UserException {

  private InvalidLoginRequestException(Throwable cause) {
    super(ErrorCode.INVALID_LOGIN_REQUEST, cause);
  }

  public static InvalidLoginRequestException jsonParseFailed(Throwable cause) {
    return new InvalidLoginRequestException(cause);
  }
}