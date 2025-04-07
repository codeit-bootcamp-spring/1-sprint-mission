package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.UUID;

public class PrivateChannelUpdateException extends ChannelException {
    public PrivateChannelUpdateException(UUID id) {
        super(ErrorCode.PRIVATE_CHANNEL_UPDATE);
        this.getDetails().put("id", id);
    }
}
