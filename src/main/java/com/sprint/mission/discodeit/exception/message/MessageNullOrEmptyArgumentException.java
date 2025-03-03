package com.sprint.mission.discodeit.exception.message;

import org.springframework.http.HttpStatus;

public class MessageNullOrEmptyArgumentException extends MessageException {

    public MessageNullOrEmptyArgumentException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
