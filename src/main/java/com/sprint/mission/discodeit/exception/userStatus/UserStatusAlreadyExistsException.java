package com.sprint.mission.discodeit.exception.userStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class UserStatusAlreadyExistsException extends UserStatusException {
    public UserStatusAlreadyExistsException(UUID userId) {
        super(ErrorCode.USER_STATUS_ALREADY_EXISTS, Map.of("userId", userId));
    }
}
