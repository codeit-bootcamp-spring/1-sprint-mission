package com.sprint.mission.discodeit.exception.binary;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class DuplicateBinaryFileException extends BinaryContentException {

  public DuplicateBinaryFileException(String fileName) {
    super(ErrorCode.DUPLICATE_BINARY_FILES, Map.of("fileName", fileName));
  }
}
