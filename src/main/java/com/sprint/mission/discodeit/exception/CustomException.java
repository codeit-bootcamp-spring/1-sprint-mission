package com.sprint.mission.discodeit.exception;

public class CustomException extends RuntimeException {

    private final ExceptionText exceptionCode;

    public CustomException(ExceptionText exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    public String getMessage() {
        return exceptionCode.getMessage();
    }
}
