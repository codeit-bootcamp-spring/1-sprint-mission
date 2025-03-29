package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ChannelNotFoundException extends ChannelException {

  public ChannelNotFoundException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

  public static ChannelNotFoundException of(Map<String, Object> details) {
    return new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND, details);
  }
}
