package com.sprint.mission.discodeit.global.exception.userstatus;

import com.sprint.mission.discodeit.global.exception.BusinessException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import java.util.Map;

public class UserStatusException extends BusinessException {

  public UserStatusException(ErrorCode errorCode) {
    super(errorCode);
  }

  public UserStatusException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
