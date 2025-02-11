package com.sprint.mission.service.exception;

public class NotFoundId extends NullPointerException {
    public NotFoundId(String message) {
        super(message);
    }

    public NotFoundId() {
        super("Fail to find : wrong id");
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return super.fillInStackTrace();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
