package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class TokenException extends DiscodeitException {

  public TokenException(ErrorCode errorCode) {
    super(errorCode);
  }

  public TokenException(ErrorCode errorCode, Throwable cause) {
    super(errorCode, cause);
  }

}
