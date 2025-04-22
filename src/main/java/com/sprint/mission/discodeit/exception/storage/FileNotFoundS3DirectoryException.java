package com.sprint.mission.discodeit.exception.storage;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class FileNotFoundS3DirectoryException extends StorageException {
    public FileNotFoundS3DirectoryException(UUID key) {
        super(ErrorCode.FILE_NOT_FOUND_IN_STORAGE, Map.of("key", key, "storage", "s3"));
    }
}
