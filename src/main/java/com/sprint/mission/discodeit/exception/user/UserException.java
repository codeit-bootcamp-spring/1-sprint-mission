package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor
public class UserException extends DiscodeitException {

    public UserException(ErrorCode errorCode, HashMap<String, Object> details){
        super(errorCode, details);
    }
}
