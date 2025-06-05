package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class BinaryContentException extends DiscodeitException {
    public BinaryContentException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BinaryContentException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
} 