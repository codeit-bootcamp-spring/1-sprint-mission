package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ChannelNotFoundException extends ChannelException {

  public ChannelNotFoundException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
