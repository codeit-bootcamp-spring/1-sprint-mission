package com.sprint.mission.discodeit.exception;

public class DataNotFoundException extends BaseException{
    private static final String ERROR_CODE = "DATA_NOT_FOUND";

    public DataNotFoundException(String message) {
        super(message, ERROR_CODE);
    }
}
