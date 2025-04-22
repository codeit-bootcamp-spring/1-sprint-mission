package com.sprint.mission.discodeit.exception.data;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public abstract class DataAccessException extends DiscodeitException {
    protected DataAccessException(ErrorCode errorCode) {
        super(errorCode);
    }

    protected DataAccessException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
