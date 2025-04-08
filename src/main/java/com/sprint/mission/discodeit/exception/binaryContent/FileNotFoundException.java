package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;

public class FileNotFoundException extends BinaryContentException {

  public FileNotFoundException(Map<String, Object> details) {
    super(Instant.now(), ErrorCode.FILE_NOT_FOUND, details);
  }
}
