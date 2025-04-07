package com.sprint.mission.discodeit.error.exception.user;

import com.sprint.mission.discodeit.error.ErrorCode;

public class UserNotFoundException extends UserException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
