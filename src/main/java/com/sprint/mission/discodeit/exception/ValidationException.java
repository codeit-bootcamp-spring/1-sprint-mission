package com.sprint.mission.discodeit.exception;

public class ValidationException extends BaseException{
    private static final String ERROR_MESSAGE = "VALIDATION_ERROR";

    public ValidationException(String message) {
        super(message, ERROR_MESSAGE);
    }
}
