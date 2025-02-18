package com.sprint.mission.discodeit.exception;

import org.springframework.http.HttpStatus;

public class DuplicateException extends ExpectedException {
    public DuplicateException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
