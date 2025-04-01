package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public abstract class FileException extends DiscodeitException {

  protected FileException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
