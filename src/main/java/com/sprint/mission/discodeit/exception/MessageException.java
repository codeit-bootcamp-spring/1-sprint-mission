package com.sprint.mission.discodeit.exception;

import java.util.Map;

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
