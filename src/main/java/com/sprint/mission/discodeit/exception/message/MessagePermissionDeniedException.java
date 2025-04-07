package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class MessagePermissionDeniedException extends DiscodeitException {

  public MessagePermissionDeniedException() {
    super(ErrorCode.MESSAGE_PERMISSION_DENIED);
  }
}