package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ExistUserAndChannelException extends ReadStatusException {

  public ExistUserAndChannelException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
