package com.sprint.mission.discodeit.exception;

public class IdNotFoundException extends BaseException{
    private static final String ERROR_CODE = "ID_NOT_FOUND_ERROR";

    public IdNotFoundException(String message) {
        super(message, ERROR_CODE);
    }
}
