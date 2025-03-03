package com.sprint.mission.discodeit.exception.user;

import org.springframework.http.HttpStatus;

public class UserStatusNullOrEmptyArgumentException extends UserException {

    public UserStatusNullOrEmptyArgumentException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
