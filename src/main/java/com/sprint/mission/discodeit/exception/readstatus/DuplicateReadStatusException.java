package com.sprint.mission.discodeit.exception.readstatus;

import java.util.UUID;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class DuplicateReadStatusException extends ReadStatusException {
	public DuplicateReadStatusException() {
		super(ErrorCode.DUPLICATE_READ_STATUS);
	}

	public static DuplicateReadStatusException withUserIdAndChannelId(UUID userId, UUID channelId) {
		DuplicateReadStatusException exception = new DuplicateReadStatusException();
		exception.addDetail("userId", userId);
		exception.addDetail("channelId", channelId);
		return exception;
	}
} 
