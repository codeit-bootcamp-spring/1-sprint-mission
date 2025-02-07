package com.sprint.mission.discodeit.domain.user.exception;

import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.global.error.exception.InvalidException;

public class AlreadyUserExistsException extends InvalidException {

    public AlreadyUserExistsException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
