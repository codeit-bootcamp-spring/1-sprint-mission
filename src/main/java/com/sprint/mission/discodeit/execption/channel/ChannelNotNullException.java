package com.sprint.mission.discodeit.execption.channel;

import com.sprint.mission.discodeit.execption.ErrorCode;
import java.util.Map;

public class ChannelNotNullException extends ChannelException {

  public ChannelNotNullException() {
    super(ErrorCode.CHANNEL_NOT_NULL);
  }

  public ChannelNotNullException(Map<String, Object> details) {
    super(ErrorCode.CHANNEL_NOT_NULL, details);
  }
}
