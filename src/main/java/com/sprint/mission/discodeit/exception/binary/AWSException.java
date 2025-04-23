package com.sprint.mission.discodeit.exception.binary;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;

public class AWSException extends DiscodeitException {

  public AWSException(Instant timestamp,
      ErrorCode errorCode,
      Map<String, Object> details) {
    super(timestamp, errorCode, details);
  }
}
