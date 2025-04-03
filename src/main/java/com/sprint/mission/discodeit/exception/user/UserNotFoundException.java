package com.sprint.mission.discodeit.exception.user;


import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.HashMap;

public class UserNotFoundException extends UserException{
    public UserNotFoundException(HashMap<String, Object> details){
        super(ErrorCode.USER_NOT_FOUND, details);
    }
}
