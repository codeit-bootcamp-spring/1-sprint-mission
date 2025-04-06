package com.sprint.mission.discodeit.exception.user;

import java.util.Map;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserException extends DiscodeitException {
	public UserException(ErrorCode code) {
		super(code);
	}

	public UserException(ErrorCode code, Map<String, Object> details) {
		super(code, details);
	}
}
