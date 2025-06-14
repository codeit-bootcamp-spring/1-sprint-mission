package com.sprint.mission.discodeit.exception.notification;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;

public class NotificationNotFoundException extends NotificationException {

  public NotificationNotFoundException(Map<String, Object> details) {
    super(Instant.now(), ErrorCode.NOTIFICATION_NOT_FOUND, details);
  }
}
