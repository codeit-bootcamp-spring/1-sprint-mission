package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class BinaryContentException extends DiscodeitException {

  public BinaryContentException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
