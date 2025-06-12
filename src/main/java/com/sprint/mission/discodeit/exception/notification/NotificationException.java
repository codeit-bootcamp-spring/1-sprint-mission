package com.sprint.mission.discodeit.exception.notification;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public abstract class NotificationException extends DiscodeitException {
    protected NotificationException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}