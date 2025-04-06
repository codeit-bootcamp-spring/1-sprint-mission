package com.sprint.mission.discodeit.exception.user;

import java.time.Instant;
import java.util.Map;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserException extends DiscodeitException {

	public UserException(String message, ErrorCode errorCode, Map<String, Object> details) {
		super(message, errorCode, details);
	}
}
