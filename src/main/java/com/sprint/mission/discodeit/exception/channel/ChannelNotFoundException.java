package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class ChannelNotFoundException extends ChannelException {

  public ChannelNotFoundException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

  public static ChannelNotFoundException of(UUID channelId) {
    Map<String, Object> details = Map.of("Channel Id", channelId);
    return new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND, details);
  }
}
