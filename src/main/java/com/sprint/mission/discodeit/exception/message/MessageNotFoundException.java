package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.error.ErrorCode;
import java.util.Map;

public class MessageNotFoundException extends MessageException {

  public MessageNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }

  public MessageNotFoundException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
