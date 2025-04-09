package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.error.ErrorCode;
import java.util.Map;

public class ChannelNotFoundException extends ChannelException {

  public ChannelNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }

  public ChannelNotFoundException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
