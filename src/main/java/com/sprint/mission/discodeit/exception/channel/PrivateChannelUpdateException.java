package com.sprint.mission.discodeit.exception.channel;

import java.util.UUID;

import com.sprint.mission.discodeit.exception.ErrorCode;

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
