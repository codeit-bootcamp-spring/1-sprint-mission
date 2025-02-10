package com.sprint.mission.discodeit.domain.channel.exception;

import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.global.error.exception.InvalidException;

public class ChannelSubjectOverLengthException extends InvalidException {

    public ChannelSubjectOverLengthException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
