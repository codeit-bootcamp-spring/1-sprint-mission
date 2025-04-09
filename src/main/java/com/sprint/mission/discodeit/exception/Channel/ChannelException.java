package com.sprint.mission.discodeit.exception.Channel;

import java.util.Map;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class ChannelException extends DiscodeitException {
	public ChannelException(ErrorCode errorCode) {
		super(errorCode);
	}

	public ChannelException(ErrorCode errorCode, Map<String, Object> details) {
		super(errorCode);
	}
}