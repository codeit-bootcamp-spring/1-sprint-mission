package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

	private final Instant timestamp;
	private final int status;
	private final String exceptionType;
	private final String code;
	private final String message;
	private final Map<String, Object> details;

	public ErrorResponse(int status, String exceptionType, String code, String message) {
		this.timestamp = Instant.now();
		this.status = status;
		this.exceptionType = exceptionType;
		this.code = code;
		this.message = message;
		this.details = null;
	}

	public ErrorResponse(int status, String simpleName, String code, String message, Map<String, Object> details) {
		this.timestamp = Instant.now();
		this.status = status;
		this.exceptionType = simpleName;
		this.code = code;
		this.message = message;
		this.details = details;
	}
}
