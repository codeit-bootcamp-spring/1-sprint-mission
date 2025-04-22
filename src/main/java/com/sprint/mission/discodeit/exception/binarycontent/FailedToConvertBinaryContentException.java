package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class FailedToConvertBinaryContentException extends BinaryContentException {
    public FailedToConvertBinaryContentException() {
        super(ErrorCode.FILE_CONVERT_FAILED);
    }
}
