package com.sprint.mission.discodeit.global.error.execption.userStatus;

import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.global.error.execption.InvalidValueException;

public class UserStatusAlreadyExistException extends InvalidValueException {

    public UserStatusAlreadyExistException(String value) {
        super(ErrorCode.USERSTATUS_ALREADY_EXIST.format(value), ErrorCode.USERSTATUS_ALREADY_EXIST);
    }
}
