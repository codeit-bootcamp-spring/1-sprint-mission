package com.sprint.mission.discodeit.exception;

public enum ExceptionText {

    INVALID_EMAIL("유효하지 않은 이메일 형식입니다."),
    INVALID_PASSWORD("비밀번호는 최소 8자 이상, 대문자, 소문자, 숫자 및 특수문자를 포함해야 합니다."),
    INVALID_NAME("이름은 최소 2자 이상이어야 합니다."),
    CHANNEL_NOT_FOUND("해당되는 id 의 채널을 찾을 수 없습니다."),
    USER_NOT_FOUND("해당되는 id 의 사용자를 찾을 수 없습니다."),
    INVALID_MESSAGE_CONTENT("메시지 내용은 null 또는 빈 값일 수 없습니다."),
    MESSAGE_CREATION_FAILED("메시지 생성 실패"),
    USER_CREATION_FAILED("유저 생성 실패"),
    MESSAGE_NOT_FOUND("등록된 메시지가 없습니다."),
    DUPLICATE_NAME("이미 동일한 이름이 존재합니다. "),
    DUPLICATE_EMAIL("이미 동일한 이메일이 존재합니다."),
    CHANNEL_CREATION_FAILED("채널 생성 실패");

    private final String message;

    ExceptionText(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}

