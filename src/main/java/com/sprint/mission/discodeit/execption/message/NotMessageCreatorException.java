package com.sprint.mission.discodeit.execption.message;

import com.sprint.mission.discodeit.execption.ErrorCode;
import java.util.Map;

public class NotMessageCreatorException extends MessageException {

    public NotMessageCreatorException(Map<String, Object> details) {
        super(ErrorCode.NOT_MESSAGE_CREATOR, details);
    }

}
