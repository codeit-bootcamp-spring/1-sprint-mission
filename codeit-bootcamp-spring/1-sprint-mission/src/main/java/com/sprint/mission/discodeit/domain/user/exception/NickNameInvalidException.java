package com.sprint.mission.discodeit.domain.user.exception;

import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.global.error.exception.InvalidException;

public class NickNameInvalidException extends InvalidException {

    public NickNameInvalidException(ErrorCode errorCode, String inputValue) {
        super(errorCode, inputValue);
    }
}
