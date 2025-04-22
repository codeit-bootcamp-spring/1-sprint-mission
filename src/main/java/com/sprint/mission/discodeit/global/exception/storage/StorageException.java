package com.sprint.mission.discodeit.global.exception.storage;

import com.sprint.mission.discodeit.global.exception.BusinessException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import java.util.Map;

public class StorageException extends BusinessException {

  public StorageException(ErrorCode errorCode) {
    super(errorCode);
  }

  public StorageException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}
