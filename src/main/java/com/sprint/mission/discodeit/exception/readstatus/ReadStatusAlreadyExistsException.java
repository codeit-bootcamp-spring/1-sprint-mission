package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class ReadStatusAlreadyExistsException extends ReadStatusException{

  private ReadStatusAlreadyExistsException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

  public static ReadStatusException of(Map<String, Object> details) {
    return new ReadStatusException(ErrorCode.DUPLICATE_READ_STATUS, details);
  }

  public static ReadStatusException of(UUID userId, UUID channelId) {
    Map<String, Object> details = Map.of("User Id", userId, "Channel Id", channelId);
    return new ReadStatusException(ErrorCode.DUPLICATE_READ_STATUS, details);
  }
}
