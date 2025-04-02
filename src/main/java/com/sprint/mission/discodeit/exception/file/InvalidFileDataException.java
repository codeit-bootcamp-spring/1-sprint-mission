package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class InvalidFileDataException extends FileException {

  public InvalidFileDataException() {
    super(ErrorCode.INVALID_FILE_DATA);
  }
}
