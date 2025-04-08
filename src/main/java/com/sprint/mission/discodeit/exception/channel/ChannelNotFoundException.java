package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.UUID;

public class ChannelNotFoundException extends ChannelException {
    public ChannelNotFoundException(UUID id) {
        super(ErrorCode.CHANNEL_NOT_FOUND);
        this.getDetails().put("id", id);
    }
}
