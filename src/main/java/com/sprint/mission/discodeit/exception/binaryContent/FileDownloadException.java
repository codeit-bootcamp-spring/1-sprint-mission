package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class FileDownloadException extends DiscodeitException {

  public FileDownloadException(Throwable cause) {
    super(ErrorCode.FILE_DOWNLOAD_ERROR);
    this.addDetail("cause", cause.getMessage());
  }
}