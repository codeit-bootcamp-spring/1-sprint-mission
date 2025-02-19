package com.sprint.mission.discodeit.domain.message.exception;

import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.global.error.exception.EntityNotFoundException;

public class MessageNotFoundException extends EntityNotFoundException {

    public MessageNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
