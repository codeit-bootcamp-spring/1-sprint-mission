package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Collections;

public class UserAlreadyExistException extends UserException {

  public UserAlreadyExistException(String field, String value) {
    super(ErrorCode.DUPLICATE_USER, Collections.singletonMap(field, value));
  }
}
