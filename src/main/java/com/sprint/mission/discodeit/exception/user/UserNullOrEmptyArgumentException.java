package com.sprint.mission.discodeit.exception.user;

import org.springframework.http.HttpStatus;

public class UserNullOrEmptyArgumentException extends UserException {

    public UserNullOrEmptyArgumentException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
