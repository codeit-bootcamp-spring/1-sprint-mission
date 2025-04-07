package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.UUID;

public class UserNotFoundException extends UserException {
    public UserNotFoundException(UUID id) {
        super(ErrorCode.USER_NOT_FOUND);
        this.getDetails().put("id", id);
    }

    public UserNotFoundException(String username) {
        super(ErrorCode.USER_NOT_FOUND);
        this.getDetails().put("username", username);
    }
}
