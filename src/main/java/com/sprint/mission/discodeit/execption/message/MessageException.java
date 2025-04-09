package com.sprint.mission.discodeit.execption.message;

import com.sprint.mission.discodeit.execption.DiscodeitException;
import com.sprint.mission.discodeit.execption.ErrorCode;
import java.util.Map;

public class MessageException extends DiscodeitException {

  public MessageException(ErrorCode errorCode) {
    super(errorCode);
  }

  public MessageException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
