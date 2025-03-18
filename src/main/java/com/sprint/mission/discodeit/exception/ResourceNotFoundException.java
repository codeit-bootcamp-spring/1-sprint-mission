package com.sprint.mission.discodeit.exception;

public class ResourceNotFoundException extends BaseException{
    private static final String ERROR_MESSAGE = "RESOURCE_NOT_FOUND";

    public ResourceNotFoundException(String message) {
        super(message, ERROR_MESSAGE);
    }
}
