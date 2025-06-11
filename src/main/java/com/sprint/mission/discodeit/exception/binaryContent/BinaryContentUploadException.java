package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class BinaryContentUploadException extends BinaryContentException {

  public BinaryContentUploadException(ErrorCode errorCode) {
    super(errorCode);
  }

  public BinaryContentUploadException(ErrorCode errorCode, Throwable cause) {
    super(errorCode, cause);
  }
}
