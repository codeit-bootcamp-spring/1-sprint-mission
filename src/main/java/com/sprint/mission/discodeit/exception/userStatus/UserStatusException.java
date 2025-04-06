package com.sprint.mission.discodeit.exception.userStatus;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor
public class UserStatusException extends DiscodeitException {
    public UserStatusException(ErrorCode errorCode, HashMap<String, Object> details){
        super(errorCode, details);
    }
}
