package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class EmptyFileUploadException extends BinaryContentException {
    public EmptyFileUploadException(String rejectFilename) {
        super(ErrorCode.FILE_IS_EMPTY, Map.of("rejectFilename", rejectFilename));
    }
}
