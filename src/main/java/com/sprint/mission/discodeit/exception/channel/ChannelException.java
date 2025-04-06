package com.sprint.mission.discodeit.exception.channel;

import java.time.Instant;
import java.util.Map;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class ChannelException extends DiscodeitException {

	public ChannelException(String message, ErrorCode errorCode, Map<String, Object> details) {
		super(message, errorCode, details);
	}
}