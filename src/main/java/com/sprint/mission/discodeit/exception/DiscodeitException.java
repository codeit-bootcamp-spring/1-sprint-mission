package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;

import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

	private final ErrorCode errorCode;
	private final Map<String, Object> details;

	public DiscodeitException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
		this.details = null;
	}

	public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
		this.details = details;
	}
}