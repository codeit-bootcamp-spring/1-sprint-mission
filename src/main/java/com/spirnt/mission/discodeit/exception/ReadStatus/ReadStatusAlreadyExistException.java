package com.spirnt.mission.discodeit.exception.ReadStatus;

import com.spirnt.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ReadStatusAlreadyExistException extends ReadStatusException {

    public ReadStatusAlreadyExistException(Map<String, Object> details) {
        super(ErrorCode.READSTATUS_ALREADY_EXISTS, details);
    }
}
