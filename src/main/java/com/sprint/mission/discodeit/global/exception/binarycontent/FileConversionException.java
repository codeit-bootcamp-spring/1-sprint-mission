package com.sprint.mission.discodeit.global.exception.binarycontent;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import java.util.Map;

public class FileConversionException extends BinaryContentException {

  public FileConversionException(ErrorCode errorCode) {
    super(errorCode);
  }

  public FileConversionException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
