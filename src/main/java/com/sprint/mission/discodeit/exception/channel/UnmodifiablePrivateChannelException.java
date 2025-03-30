package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class UnmodifiablePrivateChannelException extends ChannelException {

  public UnmodifiablePrivateChannelException(
      ErrorCode errorCode,
      Map<String, Object> details
  ) {
    super(errorCode, details);
  }

  public static UnmodifiablePrivateChannelException of(UUID channelId) {
    Map<String, Object> details = Map.of("Channel Id", channelId);
    return new UnmodifiablePrivateChannelException(ErrorCode.PRIVATE_CHANNEL_UPDATE, details);
  }
}
