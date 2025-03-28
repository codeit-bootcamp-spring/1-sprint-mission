package com.sprint.mission.discodeit.exception;

public class PrivateChannelUpdateException extends ChannelException {

	public PrivateChannelUpdateException() {
		super(ErrorCode.PRIVATE_CHANNEL_UPDATE);
	}
}