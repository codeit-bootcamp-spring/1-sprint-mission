package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import java.util.Map;

public class FileException extends DiscodeitException {

  public FileException(ErrorCode errorCode) {
    super(errorCode);
  }

  public FileException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
