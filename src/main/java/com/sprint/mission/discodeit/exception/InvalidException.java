package com.sprint.mission.discodeit.exception;

public class InvalidException extends CustomRuntimeException {
    public InvalidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
