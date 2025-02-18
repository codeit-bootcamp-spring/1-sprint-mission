package com.sprint.mission.discodeit.exception;

import org.springframework.http.HttpStatus;

public class FileIOException extends ExpectedException {
    public FileIOException(String msg) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, msg);
    }
}
