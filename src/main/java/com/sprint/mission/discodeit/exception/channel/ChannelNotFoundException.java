package com.sprint.mission.discodeit.exception.channel;

import java.util.Map;
import java.util.UUID;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class ChannelNotFoundException extends ChannelException {
	public ChannelNotFoundException(UUID channelId) {
		super(ErrorCode.CHANNEL_NOT_FOUND, Map.of("channelId", channelId));
	}
}
