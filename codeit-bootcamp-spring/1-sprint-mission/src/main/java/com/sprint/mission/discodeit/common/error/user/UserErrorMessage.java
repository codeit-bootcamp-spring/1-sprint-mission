package com.sprint.mission.discodeit.common.error.user;

public enum UserErrorMessage {

    /**
     * 1000 ~ 1999 User 관련 에러
     */
    USER_NOT_FOUND(1000, "존재하지 않는 유저입니다."),

    USER_NAME_NULL(1001, "유저의 이름은 반드시 존재해야합니다."),

    NAME_LENGTH_ERROR_MESSAGE(1002, "유저 이름의 길이 제한을 확인해주세요."),
    ;

    private final int errorCode;
    private final String message;


    UserErrorMessage(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
