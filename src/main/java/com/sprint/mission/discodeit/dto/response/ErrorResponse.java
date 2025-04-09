package com.sprint.mission.discodeit.dto.response;

import java.time.Instant;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
	private final int status;
	private final String errorCode;
	private final String message;
	private final String exceptionType;
	private final Map<String, Object> details;
	private final Instant timestamp;
}