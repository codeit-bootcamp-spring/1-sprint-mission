package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ReadStatusNotFoundException extends ReadStatusException {
  public ReadStatusNotFoundException(Map<String, Object> details) {
    super(ErrorCode.MESSAGE_NOT_FOUND, details);
  }
}
