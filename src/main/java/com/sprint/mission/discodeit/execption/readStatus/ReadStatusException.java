package com.sprint.mission.discodeit.execption.readStatus;

import com.sprint.mission.discodeit.execption.DiscodeitException;
import com.sprint.mission.discodeit.execption.ErrorCode;
import java.util.Map;

public class ReadStatusException extends DiscodeitException {

  public ReadStatusException(ErrorCode errorCode) {
    super(errorCode);
  }

  public ReadStatusException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
