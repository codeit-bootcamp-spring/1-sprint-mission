package com.sprint.mission.discodeit.execption.bianryContent;

import com.sprint.mission.discodeit.execption.DiscodeitException;
import com.sprint.mission.discodeit.execption.ErrorCode;
import java.util.Map;

public class BinaryContentException extends DiscodeitException {

  public BinaryContentException(ErrorCode errorCode) {
    super(errorCode);
  }

  public BinaryContentException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
