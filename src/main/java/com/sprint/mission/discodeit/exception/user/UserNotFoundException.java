package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.UUID;
import java.util.Map;


public class UserNotFoundException extends UserException {
    public UserNotFoundException(UUID userId) {
        super(ErrorCode.USER_NOT_FOUND, Map.of("UserId", userId));
    }
}
