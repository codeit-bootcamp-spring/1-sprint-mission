package com.sprint.mission.discodeit.global.error.execption;

import com.sprint.mission.discodeit.global.error.ErrorCode;

public class EntityNotFoundException extends BusinessException{

    public EntityNotFoundException(String value, ErrorCode errorCode) {
        super(value, errorCode);
    }
}
