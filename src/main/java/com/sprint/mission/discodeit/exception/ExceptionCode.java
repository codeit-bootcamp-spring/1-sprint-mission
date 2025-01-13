package com.sprint.mission.discodeit.exception;

public enum ExceptionCode {

    INVALID_EMAIL("유효하지 않은 이메일 형식입니다."),
    INVALID_PASSWORD("비밀번호는 최소 8자 이상, 대문자, 소문자, 숫자 및 특수문자를 포함해야 합니다."),
    INVALID_NAME("이름은 최소 2자 이상이어야 합니다."),
    POST_NOT_FOUND("해당되는 id 의 글을 찾을 수 없습니다."),
    USER_NOT_FOUND("해당되는 id 의 사용자를 찾을 수 없습니다."),
    USER_CREATION_FAILED("유저 생성 실패");

    private final String message;

    ExceptionCode(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}

