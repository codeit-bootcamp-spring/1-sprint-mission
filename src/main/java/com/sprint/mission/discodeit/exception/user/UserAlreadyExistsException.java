package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class UserAlreadyExistsException extends UserException {
    public UserAlreadyExistsException(String email, String username) {
        super(ErrorCode.USER_IS_ALREADY_EXIST, Map.of("email", email, "username", username));
    }
}
