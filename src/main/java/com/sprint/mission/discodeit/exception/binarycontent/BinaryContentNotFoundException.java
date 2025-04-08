package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class BinaryContentNotFoundException extends BinaryContentException {

  public BinaryContentNotFoundException() {
    super(ErrorCode.BINARYCONTENT_NOT_FOUND);
  }

  public BinaryContentNotFoundException(Map<String, Object> details) {
    super(ErrorCode.BINARYCONTENT_NOT_FOUND, details);
  }
}
