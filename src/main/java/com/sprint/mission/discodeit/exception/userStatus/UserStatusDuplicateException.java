package com.sprint.mission.discodeit.exception.userStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserStatusDuplicateException extends UserStatusException {

  public UserStatusDuplicateException(String fieldName, String value) {
    super(ErrorCode.USER_STATUS_DUPLICATE, Map.of("field", fieldName, "value", value));
  }
}
