package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.error.ErrorCode;
import java.util.Map;

public class AuthException extends DiscodeitException {

  public AuthException(ErrorCode errorCode) {
    super(errorCode);
  }

  public AuthException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
