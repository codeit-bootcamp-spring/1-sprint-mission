package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class RefreshTokenSessionNotFoundException extends AuthException {

  public RefreshTokenSessionNotFoundException() {
    super(ErrorCode.REFRESH_TOKEN_SESSION_NOT_FOUND);
  }
}
