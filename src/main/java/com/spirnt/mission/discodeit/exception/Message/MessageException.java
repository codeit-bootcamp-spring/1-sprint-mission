package com.spirnt.mission.discodeit.exception.Message;

import com.spirnt.mission.discodeit.exception.DiscodeitException;
import com.spirnt.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class MessageException extends DiscodeitException {

    public MessageException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
