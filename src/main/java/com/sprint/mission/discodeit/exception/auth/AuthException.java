package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor
public class AuthException extends DiscodeitException {
    public AuthException(ErrorCode errorCode, HashMap<String, Object> details){
        super(errorCode, details);
    }
}
