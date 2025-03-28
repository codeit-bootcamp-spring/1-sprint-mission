package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class PrivateChannelUpdateException extends ChannelException {

  public PrivateChannelUpdateException(ErrorCode errorCode) {
    super(errorCode);
  }
}
