package com.sprint.mission.discodeit.exception.storage;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.nio.file.Path;
import java.util.Map;

public class CreateFailedLocalDirectoryException extends StorageException {
    public CreateFailedLocalDirectoryException(Path dirPath) {
        super(ErrorCode.FAILED_TO_CREATE_DIRECTORY, Map.of("dirPath", dirPath));
    }
}
