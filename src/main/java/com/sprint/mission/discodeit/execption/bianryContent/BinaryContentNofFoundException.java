package com.sprint.mission.discodeit.execption.bianryContent;

import com.sprint.mission.discodeit.execption.ErrorCode;
import java.util.Map;

public class BinaryContentNofFoundException extends BinaryContentException {

  public BinaryContentNofFoundException() {
    super(ErrorCode.BINARYCONTENT_NOT_FOUND);
  }

  public BinaryContentNofFoundException(Map<String, Object> details) {
    super(ErrorCode.BINARYCONTENT_NOT_FOUND, details);
  }
}
