package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class UnauthorizedException extends DiscodeitException {

  public UnauthorizedException() {
    super(ErrorCode.UNAUTHRORIZE);
  }

  public static UnauthorizedException withRole(Role role) {
    UnauthorizedException exception = new UnauthorizedException();
    exception.addDetail("required Role", role);
    return exception;
  }

}
