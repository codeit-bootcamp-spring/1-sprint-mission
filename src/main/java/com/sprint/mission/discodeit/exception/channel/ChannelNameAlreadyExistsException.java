package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ChannelNameAlreadyExistsException extends ChannelException {

  public ChannelNameAlreadyExistsException() {
    super(ErrorCode.DUPLICATE_CHANNEL_NAME);
  }

  public ChannelNameAlreadyExistsException(Map<String, Object> details) {
    super(ErrorCode.DUPLICATE_CHANNEL_NAME, details);
  }

}
