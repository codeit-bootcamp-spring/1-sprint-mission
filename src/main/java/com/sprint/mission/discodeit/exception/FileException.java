package com.sprint.mission.discodeit.exception;

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