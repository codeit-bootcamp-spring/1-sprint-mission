package com.sprint.mission.discodeit.exception.data;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class DataSaveFailedException extends DataAccessException {
    public DataSaveFailedException(String entityName, UUID id) {
        super(ErrorCode.FAILED_TO_SAVE_DATA, Map.of("entity", entityName, "id", id));
    }

    public DataSaveFailedException(String entityName, UUID id, Throwable cause) {
        super(ErrorCode.FAILED_TO_SAVE_DATA, Map.of("entity", entityName, "id", id));
        initCause(cause);
    }
}
