package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.UUID;

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