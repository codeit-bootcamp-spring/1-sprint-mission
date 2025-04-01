package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class PrivateChannelImmutableException extends ChannelException {

  public PrivateChannelImmutableException(UUID channelId) {
    super(ErrorCode.PRIVATE_CHANNEL_IMMUTABLE, Map.of("channelId", channelId));
  }
}