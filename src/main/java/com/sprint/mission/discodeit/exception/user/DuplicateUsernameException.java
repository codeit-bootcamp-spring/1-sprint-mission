package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class DuplicateUsernameException extends UserException {
    public DuplicateUsernameException(String username) {
        super(ErrorCode.DUPLICATE_USERNAME);
        this.getDetails().put("username", username);
    }
}
