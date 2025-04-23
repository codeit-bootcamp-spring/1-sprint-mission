package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class ReadStatusAlreadyExistException extends ReadStatusException {

  public ReadStatusAlreadyExistException(UUID userId, UUID channelId) {
    super(ErrorCode.DUPLICATION_READSTATUS, Map.of("userId", userId, "channelId", channelId));
  }
}
