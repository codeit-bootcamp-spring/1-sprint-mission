package com.sprint.mission.discodeit.exception.channel;

import java.util.Map;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class ChannelException extends DiscodeitException {
	public ChannelException(ErrorCode code) {
		super(code);
	}

	public ChannelException(ErrorCode code, Map<String, Object> details) {
		super(code, details);
	}
}
