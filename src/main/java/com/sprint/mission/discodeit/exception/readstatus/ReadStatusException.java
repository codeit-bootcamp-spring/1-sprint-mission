package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class ReadStatusException extends DiscodeitException {

  protected ReadStatusException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

}
