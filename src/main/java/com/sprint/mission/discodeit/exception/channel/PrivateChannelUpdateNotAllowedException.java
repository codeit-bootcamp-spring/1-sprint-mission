package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Collections;
import java.util.UUID;

public class PrivateChannelUpdateNotAllowedException extends ChannelException {

  public PrivateChannelUpdateNotAllowedException(UUID channelId) {
    super(ErrorCode.PRIVATE_CHANNEL_UPDATE, Collections.singletonMap("channelId", channelId));
  }
}
