package com.sprint.mission.discodeit.exception.userStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.HashMap;

public class UserStatusNotFoundException extends UserStatusException {

    public UserStatusNotFoundException(HashMap<String, Object> details) {
        super(ErrorCode.USERSTATUS_NOT_FOUND, details);
    }
}
