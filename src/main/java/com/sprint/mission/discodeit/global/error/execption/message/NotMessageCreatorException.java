package com.sprint.mission.discodeit.global.error.execption.message;

import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.global.error.execption.InvalidValueException;

public class NotMessageCreatorException extends InvalidValueException {

    public NotMessageCreatorException(String value) {
        super(ErrorCode.NOT_MESSAGE_CREATOR.format(value), ErrorCode.NOT_MESSAGE_CREATOR);
    }
}
