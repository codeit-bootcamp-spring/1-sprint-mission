package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class PrivateChannelUpdateNotAllowedException extends ChannelException {
  public PrivateChannelUpdateNotAllowedException(Map<String, Object> details) {
    super(ErrorCode.PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED, details);
  }
}