package com.sprint.mission.discodeit.error.exception.channel;

import com.sprint.mission.discodeit.error.ErrorCode;

public class ChannelAccessDeniedException extends ChannelException {
    public ChannelAccessDeniedException() {
        super(ErrorCode.CHANNEL_ACCESS_DENIED);
    }
}
