package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ReadStatusNotFoundException extends ReadStatusException {

  public ReadStatusNotFoundException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
