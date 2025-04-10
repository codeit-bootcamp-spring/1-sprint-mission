package com.sprint.mission.discodeit.exception.response;

import java.time.Instant;
import java.util.Map;

import com.sprint.mission.discodeit.exception.DiscodeitException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
	private Instant timestamp;
	private String code;
	private String message;
	private Map<String, Object> details;
	private String exceptionType;
	private int status;

	public static ErrorResponse of(DiscodeitException ex) {
		return new ErrorResponse(
			ex.getTimestamp(),
			ex.getErrorCode().name(),
			ex.getErrorCode().getMessage(),
			ex.getDetails(),
			ex.getClass().getSimpleName(),
			ex.getErrorCode().getStatus().value()
		);
	}
}
