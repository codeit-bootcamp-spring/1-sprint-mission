package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ReadStatusAlreadyExistsException extends ReadStatusException {

  public ReadStatusAlreadyExistsException(Map<String, Object> details) {
    super(ErrorCode.READ_STATUS_ALREADY_EXISTS, details);
  }
}
