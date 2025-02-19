package com.sprint.mission.discodeit.domain.user.exception;

import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.global.error.exception.InvalidException;

public class UserNameInvalidException extends InvalidException {

    public UserNameInvalidException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
