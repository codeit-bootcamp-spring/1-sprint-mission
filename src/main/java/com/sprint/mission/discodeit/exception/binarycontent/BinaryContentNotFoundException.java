package com.sprint.mission.discodeit.exception.binarycontent;

import java.util.UUID;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class BinaryContentNotFoundException extends BinaryContentException {
	public BinaryContentNotFoundException() {
		super(ErrorCode.BINARY_CONTENT_NOT_FOUND);
	}

	public static BinaryContentNotFoundException withId(UUID binaryContentId) {
		BinaryContentNotFoundException exception = new BinaryContentNotFoundException();
		exception.addDetail("binaryContentId", binaryContentId);
		return exception;
	}
} 
