package com.sprint.mission.discodeit.exception;

public class CustomException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public CustomException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    public String getMessage() {
        return exceptionCode.getMessage();
    }
}
