package com.sprint.mission.discodeit.exception.message;

import java.util.Map;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class MessageException extends DiscodeitException {
	public MessageException(ErrorCode code) {
		super(code);
	}

	public MessageException(ErrorCode code, Map<String, Object> details) {
		super(code, details);
	}
}
