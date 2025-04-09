package com.sprint.mission.discodeit.exception.User;

import java.util.Map;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserAlreadyExistException extends UserException{
	public UserAlreadyExistException(ErrorCode errorCode) {
		super(errorCode);
	}
	public UserAlreadyExistException(ErrorCode errorCode, Map<String, Object> details) {
		super(errorCode, details);
	}

}