package com.sprint.mission.discodeit.global.exception.message;

import com.sprint.mission.discodeit.global.exception.BusinessException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import java.util.Map;

public class MessageException extends BusinessException {

  public MessageException(ErrorCode errorCode) {
    super(errorCode);
  }

  public MessageException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
