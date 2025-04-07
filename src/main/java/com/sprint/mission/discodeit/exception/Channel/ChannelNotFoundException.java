package com.sprint.mission.discodeit.exception.Channel;

import java.util.Map;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class ChannelNotFoundException extends ChannelException{
	public ChannelNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
	public ChannelNotFoundException(ErrorCode errorCode, Map<String, Object> details) {
		super(errorCode);
	}
}