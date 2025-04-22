package com.sprint.mission.discodeit.exception.storage;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class LoadFailedLocalDirectoryException extends StorageException {
    public LoadFailedLocalDirectoryException(UUID fileId) {
        super(ErrorCode.FAILED_TO_LOAD_STORAGE, Map.of("fileId", fileId, "storage", "local"));
    }
}
