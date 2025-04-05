package com.sprint.mission.discodeit.global.exception.channel;

import com.sprint.mission.discodeit.global.exception.BusinessException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import java.util.Map;

public class ChannelException extends BusinessException {

  public ChannelException(ErrorCode errorCode) {
    super(errorCode);
  }

  public ChannelException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
