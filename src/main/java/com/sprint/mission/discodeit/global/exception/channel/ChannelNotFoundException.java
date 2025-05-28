package com.sprint.mission.discodeit.global.exception.channel;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import java.util.Map;

public class ChannelNotFoundException extends ChannelException {

  public ChannelNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }

  public ChannelNotFoundException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
