package com.sprint.mission.discodeit.exception.user;

public class IllegalUserException extends RuntimeException {
    public IllegalUserException() {
        super("Illegal user parameter");
    }

    public IllegalUserException(String message) {
        super(message);
    }
}
