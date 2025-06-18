package com.sprint.mission.discodeit.exception.notification;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class NotificationNotFoundException extends NotificationException {

  public NotificationNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }

  public static NotificationNotFoundException withId(UUID id) {
    NotificationNotFoundException exception = new NotificationNotFoundException(
        ErrorCode.NOTIFICATION_NOT_FOUND);
    throw new NotificationNotFoundException(ErrorCode.NOTIFICATION_NOT_FOUND);
  }
}
