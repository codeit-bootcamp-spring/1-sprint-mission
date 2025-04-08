package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class MessageNotFoundException extends MessageException {

  public MessageNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
