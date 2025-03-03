package com.sprint.mission.discodeit.exception.channel;

import org.springframework.http.HttpStatus;

public class ChannelNullOrEmptyArgumentException extends ChannelException {

    public ChannelNullOrEmptyArgumentException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
