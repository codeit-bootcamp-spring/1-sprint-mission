package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class BinaryContentNotFoundException extends DiscodeitException {

  public BinaryContentNotFoundException(UUID contentId) {
    super(ErrorCode.BINARY_CONTENT_NOT_FOUND, "BinaryContent with id " + contentId + " not found");
  }
}