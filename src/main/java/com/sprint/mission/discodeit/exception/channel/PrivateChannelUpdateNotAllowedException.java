package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class PrivateChannelUpdateNotAllowedException extends ChannelException {

  public PrivateChannelUpdateNotAllowedException() {
    super(ErrorCode.PRIVATE_CHANNEL_UPDATE);
  }

  public static PrivateChannelUpdateNotAllowedException withId(UUID channelId) {
    PrivateChannelUpdateNotAllowedException exception = new PrivateChannelUpdateNotAllowedException();
    exception.addDetail("channelId", channelId);
    return exception;
  }
}
