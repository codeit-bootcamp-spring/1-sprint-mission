package com.sprint.mission.discodeit.common.error;

public enum ErrorMessage {

    /**
     * 1000 ~ 1999 User 관련 에러
     */
    USER_NOT_FOUND(1_000, "존재하지 않는 유저입니다."),

    USER_NAME_NULL(1_001, "유저의 이름은 반드시 존재해야합니다."),

    NAME_LENGTH_ERROR_MESSAGE(1_002, "유저 이름의 길이 제한을 확인해주세요."),

    USER_NOT_PARTICIPATED_CHANNEL(1_003, "유저가 참여하지 않은 방입니다."),

    /**
     * 2000 ~ 2999 Channel 관련 에러
     */
    CHANNEL_NOT_FOUND(2_000, "존재하지 않는 채널입니다."),
    CHANNEL_NOT_EQUAL_CREATOR(2_001, "채널을 생성한 사람만 채널이름을 변경할 수 있습니다."),
    ;

    private final int errorCode;
    private final String message;


    ErrorMessage(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
