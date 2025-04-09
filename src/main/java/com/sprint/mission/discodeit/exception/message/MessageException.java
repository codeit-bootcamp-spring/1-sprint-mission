package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor
public class MessageException extends DiscodeitException {
    public MessageException(ErrorCode errorCode, HashMap<String, Object> details){
        super(errorCode, details);
    }
}
