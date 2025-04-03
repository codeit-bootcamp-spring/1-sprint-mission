package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.HashMap;

public class PrivateChannelUpdateException extends ChannelException {
    public PrivateChannelUpdateException(HashMap<String, Object> details){
        super(ErrorCode.PRIVATE_CHANNEL_UPDATE, details);
    }
}
