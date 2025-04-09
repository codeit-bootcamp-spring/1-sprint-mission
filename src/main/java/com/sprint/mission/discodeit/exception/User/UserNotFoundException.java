package com.sprint.mission.discodeit.exception.User;

import java.util.Map;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserNotFoundException extends UserException{
	public UserNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
	public UserNotFoundException(ErrorCode errorCode, Map<String, Object> details) {
		super(errorCode, details);
	}
}