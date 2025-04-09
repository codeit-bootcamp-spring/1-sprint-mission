package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public abstract class ReadStatusException extends DiscodeitException {
    protected ReadStatusException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
