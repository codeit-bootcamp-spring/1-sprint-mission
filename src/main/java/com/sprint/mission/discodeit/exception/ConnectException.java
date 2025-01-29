package com.sprint.mission.discodeit.exception;

public class ConnectException extends NetworkException {
    private final String address;

    public ConnectException(String address, String message) {
        super(message);
        this.address = address;
    }
    public String getAddress() {
        return address;
    }
}
