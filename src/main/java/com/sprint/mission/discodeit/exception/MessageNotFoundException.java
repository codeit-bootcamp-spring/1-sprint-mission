package com.sprint.mission.discodeit.exception;

public class MessageNotFoundException extends MessageException {

	public MessageNotFoundException() {
		super(ErrorCode.MESSAGE_NOT_FOUND);
	}
}
