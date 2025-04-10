package com.sprint.mission.discodeit.exception.message;

import java.util.Map;
import java.util.UUID;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class MessageNotFoundException extends MessageException {
	public MessageNotFoundException(UUID messageId) {
		super(ErrorCode.MESSAGE_NOT_FOUND, Map.of("messageId", messageId));
	}
}
