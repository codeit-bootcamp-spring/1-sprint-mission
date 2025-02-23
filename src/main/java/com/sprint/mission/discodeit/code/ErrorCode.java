package com.sprint.mission.discodeit.code;

public enum ErrorCode {

    //사용자 관련 에러
    USER_NOT_FOUND(1001, "사용자를 찾을 수 없습니다."),
    USER_EMAIL_ALREADY_REGISTERED(1002, "이미 등록된 이메일 입니다."),
    USER_NAME_ALREADY_REGISTERED(1003, "이미 등록된 username 입니다."),
    INVALID_USER_STATUS(1004, "유효하지 않은 사용자 상태입니다."),
    USER_NOT_IN_CHANNEL(1005, "사용자가 해당 채널에 있지 않습니다."),

    //채널 관련 에러
    CHANNEL_NOT_FOUND(2001, "채널을 찾을 수 없습니다."),
    CHANNEL_PRIVATE_NOT_UPDATABLE(2002, "PRIVATE 채널은 수정할 수 없습니다."),

    //메세지 관련 에러
    MESSAGE_NOT_FOUND(3001, "메세지를 찾을 수 없습니다."),
    MESSAGE_OWNER_NOT_MATCH(3002, "해당 메세지를 작성한 유저가 아닙니다."),
    MESSAGE_CHANNEL_NOT_MATCH(3003, "해당 메세지가 작성된 채널이 아닙니다."),

    //데이터 관련 에러
    EMPTY_DATA(4001, "DTO가 비어있습니다."),

    //ReadStatus 에러
    READ_STATUS_NOT_FOUND(5001, "해당 read status를 찾을 수 없습니다."),
    READ_STATUS_ALREADY_EXIST(5002, "해당 유저와 채널의 ReadStatus가 이미 존재합니다."),

    //UserStatus 에러
    USER_STATUS_NOT_FOUND(6001, "해당 유저의 UserStatus가 없습니다.");


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
