package com.sprint.mission.discodeit.global.error.execption;

import com.sprint.mission.discodeit.global.error.ErrorCode;

public class InvalidValueException extends BusinessException {

    public InvalidValueException(String value, ErrorCode errorCode) {
        super(value, errorCode);
    }
}
