package com.sprint.mission.discodeit.global.exception.readstatus;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import java.util.Map;

public class ReadStatusAlreadyExistsException extends ReadStatusException {

  public ReadStatusAlreadyExistsException(ErrorCode errorCode) {
    super(errorCode);
  }

  public ReadStatusAlreadyExistsException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
