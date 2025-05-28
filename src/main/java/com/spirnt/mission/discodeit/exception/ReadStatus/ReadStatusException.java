package com.spirnt.mission.discodeit.exception.ReadStatus;

import com.spirnt.mission.discodeit.exception.DiscodeitException;
import com.spirnt.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ReadStatusException extends DiscodeitException {

    public ReadStatusException(ErrorCode errorCode,
        Map<String, Object> details) {
        super(errorCode, details);
    }
}
