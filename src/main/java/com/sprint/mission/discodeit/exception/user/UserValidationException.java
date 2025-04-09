package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class UserValidationException extends UserException {
    public UserValidationException(ErrorCode errorCode, String fieldName, String rejectedValue) {
        super(errorCode, Map.of("field", fieldName, "rejectedValue", rejectedValue));
    }

    public UserValidationException(ErrorCode errorCode, String fieldName) {
        super(errorCode, Map.of("field", fieldName));
    }

}
