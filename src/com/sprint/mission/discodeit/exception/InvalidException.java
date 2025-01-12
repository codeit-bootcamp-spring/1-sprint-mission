package com.sprint.mission.discodeit.exception;

public class InvalidException extends RuntimeException {
    public InvalidException(ErrorCode errorCode) {
        super(System.lineSeparator() +
                "[" + errorCode.toString() + "] " + ": " + errorCode.getDescription());
    }
}
