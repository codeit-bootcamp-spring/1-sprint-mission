package com.sprint.mission.discodeit.exception.data;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class DataUpdateFailedException extends DataAccessException {
    public DataUpdateFailedException(String entityName, UUID id) {
        super(ErrorCode.FAILED_TO_UPDATE_DATA, Map.of("entity", entityName, "id", id));
    }

    public DataUpdateFailedException(String entityName, UUID id, Throwable cause) {
        super(ErrorCode.FAILED_TO_UPDATE_DATA, Map.of("entity", entityName, "id", id));
        initCause(cause);
    }
}
