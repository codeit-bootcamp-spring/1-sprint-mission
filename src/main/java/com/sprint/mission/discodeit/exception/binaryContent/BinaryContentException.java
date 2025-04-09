package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public abstract class BinaryContentException extends DiscodeitException {
    protected BinaryContentException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
