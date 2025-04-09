package com.sprint.mission.discodeit.execption.user;

import com.sprint.mission.discodeit.execption.DiscodeitException;
import com.sprint.mission.discodeit.execption.ErrorCode;
import java.util.Map;

public class UserException extends DiscodeitException {

  public UserException(ErrorCode errorCode) {
    super(errorCode);
  }

  public UserException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
