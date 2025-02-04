package com.sprint.mission.discodeit.exception.message;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException() {
        super("메세지가 존재하지 않습니다.");
    }

    public MessageNotFoundException(String message) {
        super(message);
    }
}
