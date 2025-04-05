package com.sprint.mission.discodeit.global.exception.auth;

import com.sprint.mission.discodeit.global.exception.BusinessException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import java.util.Map;

public class AuthException extends BusinessException {

  public AuthException(ErrorCode errorCode) {
    super(errorCode);
  }

  public AuthException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
