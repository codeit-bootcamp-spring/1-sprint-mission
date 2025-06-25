package com.sprint.mission.discodeit.exception.notification;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class NotificationNotFoundException extends NotificationException {
    public NotificationNotFoundException() {
        super(ErrorCode.NOTIFICATION_NOT_FOUND);
    }
    
    public static NotificationNotFoundException withId(UUID notificationId) {
        NotificationNotFoundException exception = new NotificationNotFoundException();
        exception.addDetail("notificationId", notificationId);
        return exception;
    }
} 