package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class BinaryContentNotFoundException extends BinaryContentException {

  public BinaryContentNotFoundException(UUID uuid) {
    super(ErrorCode.BINARY_CONTENT_NOT_FOUND, Map.of("uuid", uuid));
  }
}