package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.HashMap;

public class ReadStatusAlreadyExistException extends ReadStatusException{
    public ReadStatusAlreadyExistException(HashMap<String, Object> details) {
        super(ErrorCode.DUPLICATE_READSTATUS, details);
    }
}
