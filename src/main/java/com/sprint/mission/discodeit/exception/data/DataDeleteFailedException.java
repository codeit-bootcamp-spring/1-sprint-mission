package com.sprint.mission.discodeit.exception.data;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;
import java.util.UUID;

public class DataDeleteFailedException extends DataAccessException {
    public DataDeleteFailedException(String entityName, UUID id) {
        super(ErrorCode.FAILED_TO_DELETE_DATA, Map.of("entity", entityName, "id", id));
    }

    public DataDeleteFailedException(String entityName, UUID id, Throwable cause) {
        super(ErrorCode.FAILED_TO_DELETE_DATA, Map.of("entity", entityName, "id", id));
        initCause(cause);
    }
}
