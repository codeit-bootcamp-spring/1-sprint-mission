package com.sprint.mission.discodeit.domain.channel.exception;

import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.global.error.exception.EntityNotFoundException;

public class ChannelNotFoundException extends EntityNotFoundException {

    public ChannelNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
