package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class ReadStatusNotFoundException extends ReadStatusException {

  public ReadStatusNotFoundException(UUID readStatusId, UUID userId, UUID channelId) {
    super(ErrorCode.READ_STATUS_NOT_FOUND, Map.of(
        "readStatusId", readStatusId,
        "userId", userId,
        "channelId", channelId
    ));
  }
}
