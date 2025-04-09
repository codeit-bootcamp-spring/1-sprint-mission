package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.HashMap;

public class ChannelNotFoundException extends ChannelException {
    public ChannelNotFoundException(HashMap<String, Object> details){
        super(ErrorCode.CHANNEL_NOT_FOUND, details);
    }
}
