package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class ChannelNotFoundException extends ChannelException {

  public ChannelNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
