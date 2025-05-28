package com.sprint.mission.discodeit.error.exception.channel;

import com.sprint.mission.discodeit.error.ErrorCode;

public class DuplicateChannelNameException extends ChannelException {
    public DuplicateChannelNameException() {
        super(ErrorCode.DUPLICATE_CHANNEL_NAME);
    }
}
