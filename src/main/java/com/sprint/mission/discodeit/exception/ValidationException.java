package com.sprint.mission.discodeit.exception;

import java.util.Map;

public class ValidationException extends DiscodeitException {

    public ValidationException(String requset) {
        super(ErrorCode.VALIDATION_ERROR, Map.of("requset", requset));
    }
}
