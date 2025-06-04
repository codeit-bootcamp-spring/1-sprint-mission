package com.sprint.mission.discodeit.exception.readstatus;

import java.util.UUID;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class ReadStatusNotFoundException extends ReadStatusException {
	public ReadStatusNotFoundException() {
		super(ErrorCode.READ_STATUS_NOT_FOUND);
	}

	public static ReadStatusNotFoundException withId(UUID readStatusId) {
		ReadStatusNotFoundException exception = new ReadStatusNotFoundException();
		exception.addDetail("readStatusId", readStatusId);
		return exception;
	}
} 
