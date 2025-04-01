package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class WrongPasswordException extends UserException {
    public WrongPasswordException(String username) {
        super(ErrorCode.UNAUTHORIZED, Map.of("username", username));
    }
}
