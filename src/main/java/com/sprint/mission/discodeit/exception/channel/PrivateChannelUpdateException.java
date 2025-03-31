package com.sprint.mission.discodeit.exception.channel;

import java.util.Map;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class PrivateChannelUpdateException extends ChannelException {

	public PrivateChannelUpdateException(Map<String, Object> details) {
		super(ErrorCode.PRIVATE_CHANNEL_UPDATE, details);
	}
}