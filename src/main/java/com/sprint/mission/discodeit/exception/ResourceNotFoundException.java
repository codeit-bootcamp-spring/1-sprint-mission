package com.sprint.mission.discodeit.exception;

public class ResourceNotFoundException extends BaseException{
    private static final String ERROR_CODE = "RESOURCE_NOT_FOUND";

    public ResourceNotFoundException(String message) {
        super(message, ERROR_CODE);
    }
}
