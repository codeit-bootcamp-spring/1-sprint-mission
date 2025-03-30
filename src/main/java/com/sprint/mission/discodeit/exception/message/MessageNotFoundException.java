package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class MessageNotFoundException extends MessageException{

  public MessageNotFoundException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

  public static MessageNotFoundException of(UUID messageId) {
    Map<String, Object> details = Map.of("Message Id", messageId);
    return new MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND, details);
  }
}
