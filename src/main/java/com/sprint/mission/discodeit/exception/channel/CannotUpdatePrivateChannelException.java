package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class CannotUpdatePrivateChannelException extends ChannelException {

  public CannotUpdatePrivateChannelException(UUID channelId) {
    super(ErrorCode.CAN_NOT_UPDATE_PRIVATE_CHANNEL, Map.of("channelId", channelId));
  }
}
