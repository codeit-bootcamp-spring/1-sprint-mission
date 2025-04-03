package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor
public class ChannelException extends DiscodeitException {
    public ChannelException(ErrorCode errorCode, HashMap<String, Object> details){
        super(errorCode, details);
    }
}
