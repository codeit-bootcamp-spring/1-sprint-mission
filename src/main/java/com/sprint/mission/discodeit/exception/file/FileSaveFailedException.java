package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class FileSaveFailedException extends FileException {

  public FileSaveFailedException(String reason) {
    super(ErrorCode.FILE_SAVE_FAILED, Map.of("reason", reason));
  }
}