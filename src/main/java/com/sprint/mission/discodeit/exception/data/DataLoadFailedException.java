package com.sprint.mission.discodeit.exception.data;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class DataLoadFailedException extends DataAccessException {
    public DataLoadFailedException(String entityName, UUID id) {
        super(ErrorCode.FAILED_TO_LOAD_DATA, Map.of("entity", entityName, "id", id));
    }

    public DataLoadFailedException(String entityName, UUID id, Throwable cause) {
        super(ErrorCode.FAILED_TO_LOAD_DATA, Map.of("entity", entityName, "id", id));
        initCause(cause);
    }
}
