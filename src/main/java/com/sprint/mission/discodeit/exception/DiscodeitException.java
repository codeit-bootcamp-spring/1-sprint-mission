package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {
    private final ErrorCode errorCode;

    public DiscodeitException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public DiscodeitException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public DiscodeitException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
} 