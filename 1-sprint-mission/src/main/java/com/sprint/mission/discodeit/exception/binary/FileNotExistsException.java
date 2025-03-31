package com.sprint.mission.discodeit.exception.binary;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class FileNotExistsException extends BinaryContentException {

  public FileNotExistsException(UUID storageId) {
    super(ErrorCode.DUPLICATE_BINARY_FILES, Map.of("storageId", storageId));
  }
}
