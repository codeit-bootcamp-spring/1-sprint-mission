package com.sprint.mission.discodeit.common.error.user;

import com.sprint.mission.discodeit.common.error.ErrorCode;

public class UserException extends RuntimeException {

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public static UserException of(ErrorCode message) {
        return new UserException(message.getErrorMessage());
    }

    public static UserException of(String message, Throwable cause) {
        return new UserException(message, cause);
    }


    public static UserException ofNotJoinChannel(
            ErrorCode message, String id
    ) {
        var format = String.format(
                "%s : not participated channel id %s",
                message.getErrorMessage(), id
        );

        return new UserException(format);
    }

}
