package com.sprint.mission.discodeit.exception.channel;

public class IllegalChannelException extends RuntimeException {
    public IllegalChannelException() {
        super("Illegal channel parameter");
    }

    public IllegalChannelException(String message) {
        super(message);
    }
}
