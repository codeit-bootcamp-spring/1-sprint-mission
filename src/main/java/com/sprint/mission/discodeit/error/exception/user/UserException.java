package com.sprint.mission.discodeit.error.exception.user;

import com.sprint.mission.discodeit.error.DiscodeitException;
import com.sprint.mission.discodeit.error.ErrorCode;

public class UserException extends DiscodeitException {
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
