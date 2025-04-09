package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.io.IOException;
import java.util.Map;

public class FileProcessingException extends FileException {

  public FileProcessingException(String fileName, IOException cause) {
    super(
        ErrorCode.FILE_SAVE_ERROR,
        Map.of("fileName", fileName, "reason", cause.getMessage())
    );
    initCause(cause);
  }
}
