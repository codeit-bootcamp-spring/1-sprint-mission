package com.sprint.mission.discodeit.exception.storage;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class SaveFailedLocalDirectoryException extends StorageException {
    public SaveFailedLocalDirectoryException(UUID fileId) {
        super(ErrorCode.FAILED_TO_SAVE_STORAGE, Map.of("fileId", fileId, "storage", "local"));
    }
}
