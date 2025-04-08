package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;

public class ChannelNotFoundException extends ChannelException {

  public ChannelNotFoundException(Map<String, Object> details) {
    super(Instant.now(), ErrorCode.CHANNEL_NOT_FOUND, details);
  }
}
