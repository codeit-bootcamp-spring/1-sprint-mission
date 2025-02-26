package com.sprint.mission.discodeit.exception.readstatus;

import org.springframework.http.HttpStatus;

public class ReadStatusNullOrEmptyArgumentException extends ReadStatusException {

    public ReadStatusNullOrEmptyArgumentException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
