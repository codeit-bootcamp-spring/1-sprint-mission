package com.spirnt.mission.discodeit.exception.auth;

import com.spirnt.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class JwtSessionNotFoundException extends AuthException {

    public JwtSessionNotFoundException(Map<String, Object> details) {
        super(ErrorCode.JWT_SESSION_NOT_FOUND, details);
    }
}
