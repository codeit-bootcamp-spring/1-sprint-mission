package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class FileNotFoundException extends FileException {

  public FileNotFoundException(Map<String, Object> details) {
    super(ErrorCode.FILE_NOT_FOUND, details);
  }

}
