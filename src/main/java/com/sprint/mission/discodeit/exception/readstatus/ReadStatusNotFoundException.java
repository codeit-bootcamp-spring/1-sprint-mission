package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Collections;
import java.util.UUID;

public class ReadStatusNotFoundException extends ReadStatusException {

  public ReadStatusNotFoundException(UUID readStatusId) {
    super(ErrorCode.READSTATUS_NOT_FOUND, Collections.singletonMap("readStatusId", readStatusId));
  }
}
