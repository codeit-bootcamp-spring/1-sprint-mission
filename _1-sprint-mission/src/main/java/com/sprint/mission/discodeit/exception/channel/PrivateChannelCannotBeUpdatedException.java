package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class PrivateChannelCannotBeUpdatedException extends ChannelException {


  public PrivateChannelCannotBeUpdatedException(
      ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
