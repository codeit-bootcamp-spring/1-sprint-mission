package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;

public class ChannelModificationNotAllowedException extends ChannelException {

  public ChannelModificationNotAllowedException(Map<String, Object> details) {
    super(Instant.now(), ErrorCode.CHANNEL_MODIFICATION_NOT_ALLOWED, details);
  }
}
