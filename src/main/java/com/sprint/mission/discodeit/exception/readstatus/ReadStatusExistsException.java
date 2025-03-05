package com.sprint.mission.discodeit.exception.readstatus;

import java.util.UUID;

public class ReadStatusExistsException extends RuntimeException {

  public ReadStatusExistsException() {
    super();
  }

  public ReadStatusExistsException(UUID userId, UUID channelId) {
    super(String.format("ReadStatus with userId %s and channelId %s already exists", userId,
        channelId));
  }
}
