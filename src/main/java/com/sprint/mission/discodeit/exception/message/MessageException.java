package com.sprint.mission.discodeit.exception.message;

import java.util.Map;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public abstract class MessageException extends DiscodeitException {
	protected MessageException(ErrorCode errorCode, String message, Map<String, Object> details) {
		super(errorCode, message, details);
	}

	protected MessageException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}

	protected MessageException(ErrorCode errorCode, Map<String, Object> details) {
		super(errorCode, details);
	}

	protected MessageException(ErrorCode errorCode) {
		super(errorCode);
	}
}
