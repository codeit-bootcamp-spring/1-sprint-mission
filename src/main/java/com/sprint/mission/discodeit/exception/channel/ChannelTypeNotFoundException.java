package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class ChannelTypeNotFoundException extends ChannelException {
    public ChannelTypeNotFoundException(String value) {
        super(ErrorCode.CHANNEL_TYPE_NOT_FOUND, Map.of("rejectChannelType", value));
    }
}
