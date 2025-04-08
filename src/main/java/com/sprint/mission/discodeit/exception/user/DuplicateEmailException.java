package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class DuplicateEmailException extends UserException{
    public DuplicateEmailException(String email) {
        super(ErrorCode.DUPLICATE_EMAIL);
        this.getDetails().put("email", email);
    }
}
