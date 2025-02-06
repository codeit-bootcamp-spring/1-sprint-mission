package com.sprint.mission.discodeit.global.error.exception;

import com.sprint.mission.discodeit.global.error.ErrorCode;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
