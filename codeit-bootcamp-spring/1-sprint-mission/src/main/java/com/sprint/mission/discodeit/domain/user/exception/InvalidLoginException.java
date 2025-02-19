package com.sprint.mission.discodeit.domain.user.exception;

import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.global.error.exception.EntityNotFoundException;

public class InvalidLoginException extends EntityNotFoundException {
    public InvalidLoginException(ErrorCode errorCode) {
        super(errorCode);
    }
}
