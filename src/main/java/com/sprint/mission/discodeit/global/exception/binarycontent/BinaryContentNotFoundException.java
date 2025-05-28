package com.sprint.mission.discodeit.global.exception.binarycontent;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import java.util.Map;

public class BinaryContentNotFoundException extends BinaryContentException {

  public BinaryContentNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }

  public BinaryContentNotFoundException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
