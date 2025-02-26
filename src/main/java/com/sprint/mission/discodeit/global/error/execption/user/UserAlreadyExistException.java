package com.sprint.mission.discodeit.global.error.execption.user;

import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.global.error.execption.InvalidValueException;

public class UserAlreadyExistException extends InvalidValueException {

    public UserAlreadyExistException(String value) {
        super(ErrorCode.USER_ALREADY_EXIST.format(value), ErrorCode.USER_ALREADY_EXIST);
    }
}
