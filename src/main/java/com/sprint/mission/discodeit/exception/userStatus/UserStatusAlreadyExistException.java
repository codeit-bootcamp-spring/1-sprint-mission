package com.sprint.mission.discodeit.exception.userStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.HashMap;

public class UserStatusAlreadyExistException extends UserStatusException {

    public UserStatusAlreadyExistException(HashMap<String, Object> details) {
        super(ErrorCode.DUPLICATE_USERSTATUS, details);
    }
}
