package com.sprint.mission.discodeit.exception;

public class FileIOException extends RuntimeException {
    public FileIOException(String msg) {
        super(msg);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
