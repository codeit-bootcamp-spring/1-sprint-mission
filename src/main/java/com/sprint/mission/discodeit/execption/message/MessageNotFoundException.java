package com.sprint.mission.discodeit.execption.message;

import com.sprint.mission.discodeit.execption.ErrorCode;
import java.util.Map;

public class MessageNotFoundException extends MessageException {

  public MessageNotFoundException() {
    super(ErrorCode.MESSAGE_NOT_FOUND);
  }

  public MessageNotFoundException(Map<String, Object> details) {
    super(ErrorCode.MESSAGE_NOT_FOUND, details);
  }
}
