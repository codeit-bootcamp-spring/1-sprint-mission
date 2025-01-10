package com.sprint.mission.discodeit.log;

public class MyLog<T> {
    private final T data;
    private final String message;

    public MyLog(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
