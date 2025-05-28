package com.spirnt.mission.discodeit.exception.Channel;

import com.spirnt.mission.discodeit.exception.DiscodeitException;
import com.spirnt.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ChannelException extends DiscodeitException {

    public ChannelException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
