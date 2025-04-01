package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class FileReadFailedException extends FileException {

  public FileReadFailedException(String reason) {
    super(ErrorCode.FILE_READ_FAILED, Map.of("reason", reason));
  }
}