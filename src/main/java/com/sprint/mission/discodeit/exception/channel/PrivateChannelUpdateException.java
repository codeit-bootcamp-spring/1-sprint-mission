package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class PrivateChannelUpdateException extends DiscodeitException {

  public PrivateChannelUpdateException() {
    super(ErrorCode.PRIVATE_CHANNEL_UPDATE);
  }
}