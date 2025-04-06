package com.sprint.mission.discodeit.exception.user;

import static com.sprint.mission.discodeit.exception.ErrorCode.*;

import java.time.Instant;
import java.util.Map;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserAlreadyExistsException extends UserException {

	public UserAlreadyExistsException(String message, Map<String, Object> details) {
		super(message, DUPLICATE_USER, details);
	}
}