package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class FileUploadException extends DiscodeitException {

  public FileUploadException(Throwable cause) {
    super(ErrorCode.FILE_UPLOAD_ERROR);
    this.addDetail("cause", cause.getMessage());
  }
}