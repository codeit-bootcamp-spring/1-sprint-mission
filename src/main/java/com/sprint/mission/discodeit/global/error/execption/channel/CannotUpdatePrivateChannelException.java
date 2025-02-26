package com.sprint.mission.discodeit.global.error.execption.channel;

import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.global.error.execption.InvalidValueException;

public class CannotUpdatePrivateChannelException extends InvalidValueException {

    public CannotUpdatePrivateChannelException(String value) {
        super(ErrorCode.CANNOT_UPDATE_PRIVATE_CHANNEL.format(value), ErrorCode.CANNOT_UPDATE_PRIVATE_CHANNEL);
    }
}
