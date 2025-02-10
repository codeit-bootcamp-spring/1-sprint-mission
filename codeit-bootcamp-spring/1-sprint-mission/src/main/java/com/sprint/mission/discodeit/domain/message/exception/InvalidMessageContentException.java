package com.sprint.mission.discodeit.domain.message.exception;

import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.global.error.exception.InvalidException;

public class InvalidMessageContentException extends InvalidException {

    public InvalidMessageContentException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
