package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExpectedException extends RuntimeException {
    private final HttpStatus status;

    public ExpectedException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
