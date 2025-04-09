package com.sprint.mission.discodeit.exception.channel;

import java.util.Map;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public abstract class ChannelException extends DiscodeitException {
	protected ChannelException(ErrorCode errorCode, String message, Map<String, Object> details) {
		super(errorCode, message, details);
	}

	protected ChannelException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}

	protected ChannelException(ErrorCode errorCode, Map<String, Object> details) {
		super(errorCode, details);
	}

	protected ChannelException(ErrorCode errorCode) {
		super(errorCode);
	}
}
