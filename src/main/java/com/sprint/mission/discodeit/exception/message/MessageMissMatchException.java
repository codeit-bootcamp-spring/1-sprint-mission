package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class MessageMissMatchException extends MessageException {

  public MessageMissMatchException(ErrorCode errorCode) {
    super(errorCode);
  }
}
