package com.sprint.misson.discordeit.code;

public enum ErrorCode {

    //사용자 관련 에러
    USER_NOT_FOUND(1001, "사용자를 찾을 수 없습니다."),
    USER_EMAIL_ALREADY_REGISTERED(1002, "이미 등록된 이메일 입니다."),
    INVALID_USER_STATUS(1003, "유효하지 않은 사용자 상태입니다."),
    USER_NOT_IN_CHANNEL(1004, "사용자가 해당 채널에 있지 않습니다."),

    //채널 관련 에러
    CHANNEL_NOT_FOUND(2001,"채널을 찾을 수 없습니다."),

    //메세지 관련 에러
    MESSAGE_NOT_FOUND(3001, "메세지를 찾을 수 없습니다."),
    MESSAGE_OWNER_NOT_MATCH(3002, "해당 메세지를 작성한 유저가 아닙니다."),
    MESSAGE_CHANNEL_NOT_MATCH(3003, "해당 메세지가 작성된 채널이 아닙니다."),

    //데이터 관련 에러
    EMPTY_DATA(4001,"DTO가 비어있습니다.");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }

}
