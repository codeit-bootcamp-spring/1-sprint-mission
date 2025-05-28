package com.sprint.mission.discodeit.global.exception.readstatus;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import java.util.Map;

public class ReadStatusNotFoundException extends ReadStatusException {

  public ReadStatusNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }

  public ReadStatusNotFoundException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
