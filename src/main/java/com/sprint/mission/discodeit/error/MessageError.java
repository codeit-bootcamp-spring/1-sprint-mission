package com.sprint.mission.discodeit.error;

public enum MessageError {
    EMPTY_CONTENT("내용을 입력해주세요"),
    EMPTY_MESSAGE("보낸 메시지가 없습니다"),
    ;

    private String message;

    MessageError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
