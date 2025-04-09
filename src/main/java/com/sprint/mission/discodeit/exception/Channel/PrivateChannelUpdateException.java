package com.sprint.mission.discodeit.exception.Channel;

import java.util.Map;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class PrivateChannelUpdateException extends ChannelException{
	public PrivateChannelUpdateException(ErrorCode errorCode) {
		super(errorCode);
	}
	public PrivateChannelUpdateException(ErrorCode errorCode, Map<String, Object> details) {
		super(errorCode);
	}
}