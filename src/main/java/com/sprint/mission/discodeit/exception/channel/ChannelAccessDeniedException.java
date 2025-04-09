package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class ChannelAccessDeniedException extends DiscodeitException {

  public ChannelAccessDeniedException() {
    super(ErrorCode.CHANNEL_ACCESS_DENIED);
  }
}