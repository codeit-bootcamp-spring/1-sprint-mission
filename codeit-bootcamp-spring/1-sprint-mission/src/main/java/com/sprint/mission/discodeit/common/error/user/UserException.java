package com.sprint.mission.discodeit.common.error.user;

public class UserException extends RuntimeException {

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public static UserException of(UserErrorMessage message) {
        return new UserException(message.getMessage());
    }

    public static UserException of(String message, Throwable cause) {
        return new UserException(message, cause);
    }
}
