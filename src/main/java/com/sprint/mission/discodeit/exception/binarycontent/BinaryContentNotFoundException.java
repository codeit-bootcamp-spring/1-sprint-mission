package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class BinaryContentNotFoundException extends BinaryException {


  public BinaryContentNotFoundException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

  public static BinaryContentNotFoundException of(UUID binaryContentId) {
    Map<String, Object> details = Map.of("Binary Content Id", binaryContentId);
    return new BinaryContentNotFoundException(ErrorCode.BINARY_CONTENT_NOT_FOUND, details);
  }
}
