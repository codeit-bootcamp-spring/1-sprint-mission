package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.code.ErrorCode;

public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    //기본 사용
    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    //상세메세지와 함께 사용
    public CustomException(ErrorCode errorCode, String detailMessage) {
        super(errorCode.getMessage() + ": " + detailMessage);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
