package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.UUID;

public class PrivateChannelUpdateException extends ChannelException {
    public PrivateChannelUpdateException() {
        super(ErrorCode.PRIVATE_CHANNEL_UPDATE);
    }
    
    public static PrivateChannelUpdateException forChannel(UUID channelId) {
        PrivateChannelUpdateException exception = new PrivateChannelUpdateException();
        exception.addDetail("channelId", channelId);
        return exception;
    }
} 