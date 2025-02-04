package com.sprint.mission.discodeit.exception.message;

public class NullMessageContentException extends RuntimeException {
    public NullMessageContentException() {
        super("메세지 내용이 없습니다.");
    }

    public NullMessageContentException(String message) {
        super(message);
    }
}
