package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserDuplicateException extends UserException {

  public UserDuplicateException(String fieldName, String value) {
    super(ErrorCode.USER_DUPLICATE, Map.of("field", fieldName, "value", value));
  }
}