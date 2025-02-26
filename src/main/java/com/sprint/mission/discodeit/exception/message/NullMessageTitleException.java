package com.sprint.mission.discodeit.exception.message;

public class NullMessageTitleException extends RuntimeException {
    public NullMessageTitleException() {
        super("메시지 제목이 비어있습니다.");
    }

    public NullMessageTitleException(String message) {
        super(message);
    }
}
