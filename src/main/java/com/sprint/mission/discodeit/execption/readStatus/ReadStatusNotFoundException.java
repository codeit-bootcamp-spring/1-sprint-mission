package com.sprint.mission.discodeit.execption.readStatus;

import com.sprint.mission.discodeit.execption.ErrorCode;
import java.util.Map;

public class ReadStatusNotFoundException extends ReadStatusException {

  public ReadStatusNotFoundException() {
    super(ErrorCode.READSTATUS_NOT_FOUND);
  }

  public ReadStatusNotFoundException(Map<String, Object> details) {
    super(ErrorCode.READSTATUS_NOT_FOUND, details);
  }
}
