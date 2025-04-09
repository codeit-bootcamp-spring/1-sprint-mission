package com.sprint.mission.discodeit.execption.channel;

import com.sprint.mission.discodeit.execption.ErrorCode;
import java.util.Map;

public class CannotUpdatePrivateChannelException extends ChannelException {

  public CannotUpdatePrivateChannelException() {
    super(ErrorCode.CANNOT_UPDATE_PRIVATE_CHANNEL);
  }

  public CannotUpdatePrivateChannelException(Map<String, Object> details) {
    super(ErrorCode.CANNOT_UPDATE_PRIVATE_CHANNEL, details);
  }
}
