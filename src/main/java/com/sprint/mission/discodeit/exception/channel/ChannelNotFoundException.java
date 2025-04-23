package com.sprint.mission.discodeit.exception.channel;

import java.util.UUID;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class ChannelNotFoundException extends ChannelException {
    public ChannelNotFoundException() {
        super(ErrorCode.CHANNEL_NOT_FOUND);
    }

    public static ChannelNotFoundException withId(UUID channelId) {
        ChannelNotFoundException exception = new ChannelNotFoundException();
        exception.addDetail("channelId", channelId);
        return exception;
    }
} 