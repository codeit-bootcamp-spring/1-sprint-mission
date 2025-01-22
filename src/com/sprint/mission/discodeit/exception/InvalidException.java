package com.sprint.mission.discodeit.exception;

public class InvalidException extends RuntimeException {
    private final ErrorCode errorCode;

    public InvalidException(ErrorCode errorCode) {
        super("[" + errorCode.toString() + "] " + ": " + errorCode.getDescription());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
