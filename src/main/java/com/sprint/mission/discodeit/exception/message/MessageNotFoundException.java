package com.sprint.mission.discodeit.exception.message;

import java.util.UUID;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class MessageNotFoundException extends MessageException {
	public MessageNotFoundException() {
		super(ErrorCode.MESSAGE_NOT_FOUND);
	}

	public static MessageNotFoundException withId(UUID messageId) {
		MessageNotFoundException exception = new MessageNotFoundException();
		exception.addDetail("messageId", messageId);
		return exception;
	}
} 
