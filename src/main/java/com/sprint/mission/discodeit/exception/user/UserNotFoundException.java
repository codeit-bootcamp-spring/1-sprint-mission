package com.sprint.mission.discodeit.exception.user;

import java.util.UUID;
import java.util.Map;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserNotFoundException extends UserException {
	public UserNotFoundException(UUID userId) {
		super(ErrorCode.USER_NOT_FOUND, Map.of("userId", userId));
	}
}
