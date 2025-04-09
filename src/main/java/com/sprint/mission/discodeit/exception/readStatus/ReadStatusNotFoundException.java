package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.HashMap;

public class ReadStatusNotFoundException extends ReadStatusException {
    public ReadStatusNotFoundException(HashMap<String, Object> details) {
        super(ErrorCode.READSTATUS_NOT_FOUND, details);
    }
}
