package com.sprint.mission.discodeit.error.exception.channel;

import com.sprint.mission.discodeit.error.ErrorCode;

public class ChannelNotFoundException extends ChannelException {
    public ChannelNotFoundException() {
        super(ErrorCode.CHANNEL_NOT_FOUND);
    }
}
