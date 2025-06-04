package com.spirnt.mission.discodeit.exception.auth;

import com.spirnt.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class InvalidJwtTokenException extends AuthException {

    public InvalidJwtTokenException(Map<String, Object> details) {
        super(ErrorCode.INVALID_JWT_TOKEN, details);
    }
}
