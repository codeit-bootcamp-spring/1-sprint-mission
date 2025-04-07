package com.sprint.mission.discodeit.exception.userStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.UUID;

public class UserStatusNotFoundException extends UserStatusException {
    public UserStatusNotFoundException(String name, UUID id) {
        super(ErrorCode.USER_STATUS_NOT_FOUND);
        this.getDetails().put(name, id);
    }
}
