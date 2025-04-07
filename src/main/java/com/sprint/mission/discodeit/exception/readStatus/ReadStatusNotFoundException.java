package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class ReadStatusNotFoundException extends DiscodeitException {

  public ReadStatusNotFoundException(UUID readStatusId) {
    super(ErrorCode.READ_STATUS_NOT_FOUND, "ReadStatus with id " + readStatusId + " not found");
  }
}