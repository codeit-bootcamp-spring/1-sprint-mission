package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public abstract class AuthException extends DiscodeitException {
    protected AuthException(ErrorCode errorCode) {
        super(errorCode);
    }

    protected AuthException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
