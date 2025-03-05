package com.sprint.mission.discodeit.global.error.execption.message;

import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.global.error.execption.EntityNotFoundException;

public class MessageNotFoundException extends EntityNotFoundException {

    public MessageNotFoundException(String value) {
        super(ErrorCode.MESSAGE_NOT_FOUND.format(value), ErrorCode.MESSAGE_NOT_FOUND);
    }
}
