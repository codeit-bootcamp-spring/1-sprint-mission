package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class InvalidRegionCodeException extends UserException {
    public InvalidRegionCodeException(String value) {
        super(ErrorCode.INVALID_REGION_CODE, Map.of("value", value));
    }
}
