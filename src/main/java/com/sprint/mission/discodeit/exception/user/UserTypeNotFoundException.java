package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserTypeNotFoundException extends UserException {
    public UserTypeNotFoundException() {
        super(ErrorCode.USER_TYPE_NOT_FOUND);
    }
}
