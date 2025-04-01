package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class FileNotFoundCustomException extends FileException {

  public FileNotFoundCustomException(String path) {
    super(ErrorCode.FILE_NOT_FOUND, Map.of("filePath", path));
  }
}