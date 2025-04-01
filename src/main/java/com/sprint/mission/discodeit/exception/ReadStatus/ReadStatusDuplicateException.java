package com.sprint.mission.discodeit.exception.ReadStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ReadStatusDuplicateException extends ReadStatusException {

  public ReadStatusDuplicateException(Map<String, Object> fieldMap) {
    super(ErrorCode.READ_STATUS_DUPLICATE, fieldMap);
  }
}