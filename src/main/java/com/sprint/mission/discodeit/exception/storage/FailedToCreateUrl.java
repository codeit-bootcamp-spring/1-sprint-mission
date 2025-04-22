package com.sprint.mission.discodeit.exception.storage;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class FailedToCreateUrl extends StorageException {
    public FailedToCreateUrl(String file) {
        super(ErrorCode.FAILED_TO_CREATE_PRESIGNED_URL, Map.of("file", file));
    }
}
