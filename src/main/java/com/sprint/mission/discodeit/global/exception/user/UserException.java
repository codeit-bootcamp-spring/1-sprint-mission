package com.sprint.mission.discodeit.global.exception.user;

import com.sprint.mission.discodeit.global.exception.BusinessException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import java.util.Map;

public class UserException extends BusinessException {

  public UserException(ErrorCode errorCode) {
    super(errorCode);
  }

  public UserException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
