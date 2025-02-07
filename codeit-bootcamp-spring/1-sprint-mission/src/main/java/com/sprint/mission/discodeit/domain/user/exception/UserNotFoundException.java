package com.sprint.mission.discodeit.domain.user.exception;

import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.global.error.exception.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
