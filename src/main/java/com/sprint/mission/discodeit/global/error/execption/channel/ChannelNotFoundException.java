package com.sprint.mission.discodeit.global.error.execption.channel;

import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.global.error.execption.EntityNotFoundException;

public class ChannelNotFoundException extends EntityNotFoundException {

    public ChannelNotFoundException(String value) {
        super(ErrorCode.CHANNEL_NOT_FOUND.format(value), ErrorCode.CHANNEL_NOT_FOUND);
    }
}
