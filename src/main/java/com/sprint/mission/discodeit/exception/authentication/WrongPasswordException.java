package com.sprint.mission.discodeit.exception.authentication;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class WrongPasswordException extends AuthenticationException{
    public WrongPasswordException(String username) {
        super(ErrorCode.WRONG_PASSWORD);
        this.getDetails().put("username", username);
    }
}
