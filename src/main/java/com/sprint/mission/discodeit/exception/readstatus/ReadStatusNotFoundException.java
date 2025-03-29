package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ReadStatusNotFoundException extends ReadStatusException {

  private ReadStatusNotFoundException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

  public static ReadStatusException of(Map<String, Object> details) {
    return new ReadStatusException(ErrorCode.READ_STATUS_NOT_FOUND, details);
  }
}
