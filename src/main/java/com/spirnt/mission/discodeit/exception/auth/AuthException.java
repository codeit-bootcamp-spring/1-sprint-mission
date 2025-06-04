package com.spirnt.mission.discodeit.exception.auth;

import com.spirnt.mission.discodeit.exception.DiscodeitException;
import com.spirnt.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class AuthException extends DiscodeitException {

    public AuthException(ErrorCode errorCode,
        Map<String, Object> details) {
        super(errorCode, details);
    }
}
