package com.sprint.mission.discodeit.exception.storage;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.nio.file.Path;
import java.util.Map;

public class FileNotFoundInLocalDirectoryException extends StorageException {
    public FileNotFoundInLocalDirectoryException(Path filePath) {
        super(ErrorCode.FILE_NOT_FOUND_IN_STORAGE, Map.of("path", filePath, "storage", "local"));
    }
}
