package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class BinaryContentAlreadyExistsException extends DiscodeitException {

  public BinaryContentAlreadyExistsException(UUID contentId) {
    super(ErrorCode.BINARY_CONTENT_ALREADY_EXISTS,
        "BinaryContent with id " + contentId + " already exists");
  }
}