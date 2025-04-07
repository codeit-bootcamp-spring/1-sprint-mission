package com.sprint.mission.discodeit.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND(404, "User Not Found"),
    USER_ALREADY_EXISTS(409, "User Already Exists"),

    CHANNEL_NOT_FOUND(404, "Channel Not Found"),
    DUPLICATE_CHANNEL_NAME(409, "Duplicate Channel Name"),
    CHANNEL_ACCESS_DENIED(403, "Channel Access Denied"),
    DEFAULT_ERROR(500, "Default Error");

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
