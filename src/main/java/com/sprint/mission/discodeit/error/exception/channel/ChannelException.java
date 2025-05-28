package com.sprint.mission.discodeit.error.exception.channel;

import com.sprint.mission.discodeit.error.DiscodeitException;
import com.sprint.mission.discodeit.error.ErrorCode;

public class ChannelException extends DiscodeitException {
    public ChannelException(ErrorCode errorCode) {
        super(errorCode);
    }
}
