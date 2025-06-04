package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class UnauthorizedException extends DiscodeitException {

  public UnauthorizedException() {
    super(ErrorCode.NO_REFRESH_TOKEN);
  }
}
