package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserException extends DiscodeitException {


  protected UserException(ErrorCode errorCode, String message, Map<String, Object> details) {
    super(errorCode, message, details);
  }

  protected UserException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  protected UserException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

  protected UserException(ErrorCode errorCode) {
    super(errorCode);
  }
}