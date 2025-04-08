package com.sprint.mission.discodeit.exception.BinaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class BinaryContentNotFoundException extends BinaryException {

  public BinaryContentNotFoundException(Map<String, Object> details) {
    super(ErrorCode.BINARY_CONTENT_NOT_FOUND, details);
  }
}
