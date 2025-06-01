package com.sprint.mission.discodeit.global.exception.security;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import java.util.Map;

public class InvalidTokenException extends SecurityException {

    public InvalidTokenException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidTokenException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
