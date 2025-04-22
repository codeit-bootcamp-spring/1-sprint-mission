package com.sprint.mission.discodeit.exception.storage;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class LoadFailedS3Exception extends StorageException {
    public LoadFailedS3Exception(UUID fileId) {
        super(ErrorCode.FAILED_TO_LOAD_DATA, Map.of("fileId", fileId, "storge", "s3"));
    }
}
