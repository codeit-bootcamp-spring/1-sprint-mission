package com.sprint.mission.discodeit.exception.storage;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class FailedToFileConvert extends StorageException {
    public FailedToFileConvert(String file) {
        super(ErrorCode.FAILED_TO_CONVERT_FILE, Map.of("file", file));
    }
}
