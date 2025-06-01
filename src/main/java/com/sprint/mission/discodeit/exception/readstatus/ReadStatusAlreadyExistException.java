package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class ReadStatusAlreadyExistException extends ReadStatusException {

  public ReadStatusAlreadyExistException() {
    super(ErrorCode.DUPLICATION_READ_STATUS);
  }

  public static ReadStatusAlreadyExistException withUserIdAndChannelId(UUID userId,
      UUID channelId) {

    ReadStatusAlreadyExistException exception = new ReadStatusAlreadyExistException();
    exception.addDetail("userId", userId);
    exception.addDetail("channelId", channelId);

    return exception;
  }
}
