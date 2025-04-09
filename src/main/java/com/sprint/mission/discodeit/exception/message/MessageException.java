package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import java.util.Map;

public class MessageException extends DiscodeitException {

  public MessageException(ErrorCode errorCode) {
    super(errorCode);
  }

  public MessageException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
