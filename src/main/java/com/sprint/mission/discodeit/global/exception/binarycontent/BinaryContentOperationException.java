package com.sprint.mission.discodeit.global.exception.binarycontent;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import java.util.Map;

public class BinaryContentOperationException extends BinaryContentException {

  public BinaryContentOperationException(
      ErrorCode errorCode) {
    super(errorCode);
  }

  public BinaryContentOperationException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
