package com.sprint.mission.discodeit.execption.channel;

import com.sprint.mission.discodeit.execption.DiscodeitException;
import com.sprint.mission.discodeit.execption.ErrorCode;
import java.util.Map;

public class ChannelException extends DiscodeitException {

  public ChannelException(ErrorCode errorCode) {
    super(errorCode);
  }

  public ChannelException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
