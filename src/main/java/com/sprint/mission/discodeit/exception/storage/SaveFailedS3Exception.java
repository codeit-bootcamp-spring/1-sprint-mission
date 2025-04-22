package com.sprint.mission.discodeit.exception.storage;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class SaveFailedS3Exception extends StorageException {
    public SaveFailedS3Exception(UUID fileId) {
        super(ErrorCode.FAILED_TO_SAVE_DATA, Map.of("fileId", fileId, "storge", "s3"));
    }
}
