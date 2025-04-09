package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class ReadStatusAlreadyExistsException extends DiscodeitException {

  public ReadStatusAlreadyExistsException(UUID userId, UUID channelId) {
    super(ErrorCode.READ_STATUS_ALREADY_EXISTS,
        "ReadStatus with userId " + userId + " and channelId " + channelId + " already exists");
  }
}