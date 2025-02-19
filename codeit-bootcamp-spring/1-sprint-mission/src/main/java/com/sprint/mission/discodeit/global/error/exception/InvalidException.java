package com.sprint.mission.discodeit.global.error.exception;

import com.sprint.mission.discodeit.global.error.ErrorCode;

public class InvalidException extends BusinessException {

    public InvalidException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
