package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class ChannelNotFoundException extends ChannelException {

	public ChannelNotFoundException(Map<String, Object> details) {
		super(ErrorCode.CHANNEL_NOT_FOUND, details);
	}

	public ChannelNotFoundException() {
		super(ErrorCode.CHANNEL_NOT_FOUND);
	}
}
