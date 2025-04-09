package com.sprint.mission.discodeit.exception.User;

import java.util.Map;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserException extends DiscodeitException {
	public UserException(ErrorCode errorCode) {
		super(errorCode);
	}

	public UserException(ErrorCode errorCode, Map<String, Object> details) {
		super(errorCode, details);
	}
}