package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.error.ErrorCode;
import java.util.Map;

public class PrivateChannelUpdateException extends ChannelException {

  public PrivateChannelUpdateException(ErrorCode errorCode) {
    super(errorCode);
  }

  public PrivateChannelUpdateException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
