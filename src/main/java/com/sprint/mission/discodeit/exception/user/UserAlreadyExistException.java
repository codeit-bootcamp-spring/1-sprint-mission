package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.HashMap;

public class UserAlreadyExistException extends UserException{
    public UserAlreadyExistException(HashMap<String, Object> details){
        super(ErrorCode.DUPLICATE_USER, details);
    }
}
