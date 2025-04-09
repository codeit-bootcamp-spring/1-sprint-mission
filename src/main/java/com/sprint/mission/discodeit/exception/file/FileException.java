package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class FileException extends DiscodeitException {

  protected FileException(ErrorCode errorCode, String message, Map<String, Object> details) {
    super(errorCode, message, details);
  }

  protected FileException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  protected FileException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

  protected FileException(ErrorCode errorCode) {
    super(errorCode);
  }
}