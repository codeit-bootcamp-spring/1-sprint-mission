package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class PrivateChannelCanNotModifyException extends ChannelException {
    public PrivateChannelCanNotModifyException(UUID channelId, Channel.ChannelType channelType) {
        super(ErrorCode.PRIVATE_CANNOT_MODIFY, Map.of("channelId", channelId, "channelType", channelType));
    }
}
