package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserAlreadyMemberException extends ReadStatusException {

    public UserAlreadyMemberException() {
        super(ErrorCode.USER_ALREADY_MEMBER);
    }
}
