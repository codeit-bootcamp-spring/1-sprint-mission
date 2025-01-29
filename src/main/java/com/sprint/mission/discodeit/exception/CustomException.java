package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    // 언체크 예외
    private ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
