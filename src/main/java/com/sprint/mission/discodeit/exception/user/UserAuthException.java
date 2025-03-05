package com.sprint.mission.discodeit.exception.user;

public class UserAuthException extends RuntimeException {

    public UserAuthException() {
        super();
    }

    public UserAuthException(String message) {
        super(message);
    }
}
