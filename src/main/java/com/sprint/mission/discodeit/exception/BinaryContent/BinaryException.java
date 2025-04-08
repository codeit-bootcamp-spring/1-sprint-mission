package com.sprint.mission.discodeit.exception.BinaryContent;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class BinaryException extends DiscodeitException {
  public BinaryException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
