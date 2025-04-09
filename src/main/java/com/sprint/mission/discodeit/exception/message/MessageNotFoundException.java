package com.sprint.mission.discodeit.exception.message;

import java.util.Map;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class MessageNotFoundException extends MessageException {

	public MessageNotFoundException(Map<String, Object> details) {
		super(ErrorCode.MESSAGE_NOT_FOUND, details);
	}
}
