package com.sprint.mission.discodeit.global.exception.security;

import com.sprint.mission.discodeit.global.exception.BusinessException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import java.util.Map;

public class SecurityException extends BusinessException {

    public SecurityException(ErrorCode errorCode) {
        super(errorCode);
    }

    public SecurityException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
