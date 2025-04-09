package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor
public class ReadStatusException extends DiscodeitException {
    public ReadStatusException(ErrorCode errorCode, HashMap<String, Object> details){
        super(errorCode, details);
    }
}
