package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class MessageContentInvalidException extends DiscodeitException {

  public MessageContentInvalidException() {
    super(ErrorCode.MESSAGE_CONTENT_INVALID);
  }
}