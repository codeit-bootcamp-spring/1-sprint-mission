package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    WRONG_PASSWORD("AUTH_001", "Wrong password", HttpStatus.UNAUTHORIZED),
    BINARY_CONTENT_NOT_FOUND("BINARY_001", "Binary content not found", HttpStatus.NOT_FOUND),
    CHANNEL_NOT_FOUND("CHANNEL_001", "Channel not found", HttpStatus.NOT_FOUND),
    PRIVATE_CHANNEL_UPDATE("CHANNEL_002", "Private channel cannot update", HttpStatus.FORBIDDEN),
    MESSAGE_NOT_FOUND("MESSAGE_001", "Message not found", HttpStatus.NOT_FOUND),
    READ_STATUS_NOT_FOUND("READ_STATUS_001", "Read status not found", HttpStatus.NOT_FOUND),
    READ_STATUS_ALREADY_EXISTS("READ_STATUS_002", "Read status already exists", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("USER_001", "User not found", HttpStatus.NOT_FOUND),
    DUPLICATE_USERNAME("USER_002", "Duplicate username", HttpStatus.BAD_REQUEST),
    DUPLICATE_EMAIL("USER_003", "Duplicate email", HttpStatus.BAD_REQUEST),
    USER_STATUS_NOT_FOUND("USER_STATUS_001", "User status not found", HttpStatus.NOT_FOUND),
    USER_STATUS_ALREADY_EXISTS("USER_STATUS_002", "User status already exists", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
