package com.sprint.mission.discodeit.exception.authentication;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class AuthenticationException extends DiscodeitException {

    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }
}

