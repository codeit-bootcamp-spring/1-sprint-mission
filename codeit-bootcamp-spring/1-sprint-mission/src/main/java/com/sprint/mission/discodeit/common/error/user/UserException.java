package com.sprint.mission.discodeit.common.error.user;

import com.sprint.mission.discodeit.common.error.ErrorMessage;

public class UserException extends RuntimeException {

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public static UserException of(ErrorMessage message) {
        return new UserException(message.getMessage());
    }

    public static UserException errorMessageAndId(ErrorMessage message, String id) {
        var format = String.format("%s : not participated channel id %s", message.getMessage(), id);
        return new UserException(format);
    }
    public static UserException errorMessageAndChannelName(ErrorMessage message, String channelName) {
        var format = String.format("%s : not participated channel name %s", message.getMessage(), channelName);
        return new UserException(format);
    }

    public static UserException of(String message, Throwable cause) {
        return new UserException(message, cause);
    }
}
