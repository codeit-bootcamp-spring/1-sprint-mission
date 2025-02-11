package com.sprint.mission.discodeit.global.error.exception;

import com.sprint.mission.discodeit.global.error.ErrorCode;

public class BusinessException extends RuntimeException {

    protected final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(errorCode.getDescription().concat(" 입력값 = ").concat(message));
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
