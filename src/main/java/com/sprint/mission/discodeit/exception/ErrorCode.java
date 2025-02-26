package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    RESOURCE_NOT_FOUND("NOT_FOUND", "잘못된 요청입니다."),
    MESSAGE_NOT_FOUND("NOT_FOUND", "메세지를 찾을 수 없습니다."),
    CHANNEL_NOT_FOUND("NOT_FOUND", "채널을 찾을 수 없습니다."),
    USER_NOT_FOUND("NOT_FOUND", "유저를 찾을 수 없습니다."),
    USER_AUTH_FAIL("BAD_REQUEST", "아이디 또는 비밀번호가 잘못되었습니다."),
    MESSAGE_NULL_CONTENT("BAD_REQUEST", "메세지 내용이 비어있습니다."),
    MESSAGE_NULL_TITLE("BAD_REQUEST", "메세지 제목이 비어있습니다."),
    ILLEGAL_USER("BAD_REQUEST", "잘못된 유저 정보입니다."),
    ILLEGAL_CHANNEL("BAD_REQUEST", "잘못된 채널 정보입니다."),
    INTERNAL_SERVER_ERROR_BINARY("INTERNAL_SERVER_ERROR", "파일에 관한 문제가 생겼습니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
