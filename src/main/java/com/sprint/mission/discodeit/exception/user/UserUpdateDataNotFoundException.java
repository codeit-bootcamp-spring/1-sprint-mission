package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class UserUpdateDataNotFoundException extends UserException {
    public UserUpdateDataNotFoundException(UUID userId) {
        super(ErrorCode.USER_UPDATE_DATA_NOT_FOUND, Map.of("userId", userId));
    }
}
