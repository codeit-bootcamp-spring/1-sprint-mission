package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.HashMap;

public class WrongPasswordException extends AuthException{
    public WrongPasswordException(HashMap<String, Object> details){
        super(ErrorCode.WRONG_PASSWORD, details);
    }
}
