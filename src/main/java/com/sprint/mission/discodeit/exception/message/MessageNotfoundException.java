package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class MessageNotfoundException extends MessageException {
  public MessageNotfoundException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
