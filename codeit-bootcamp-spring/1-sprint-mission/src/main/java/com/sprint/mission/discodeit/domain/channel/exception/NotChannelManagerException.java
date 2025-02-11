package com.sprint.mission.discodeit.domain.channel.exception;

import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.global.error.exception.InvalidException;

public class NotChannelManagerException extends InvalidException {

    public NotChannelManagerException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
