package com.sprint.mission.discodeit.exception;

import org.springframework.http.HttpStatus;

public class PrivateChannelModificationException extends ExpectedException {
    public PrivateChannelModificationException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
