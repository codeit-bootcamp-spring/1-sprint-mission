package com.sprint.mission.discodeit.common.error.user;

public enum UserErrorMessage {

    USER_NOT_FOUND("존재하지 않는 유저입니다."),
    USER_NAME_NULL("유저의 이름은 반드시 존재해야합니다."),
    NAME_LENGTH_ERROR_MESSAGE("유저 이름의 길이 제한을 확인해주세요."),
    ;

    private final String message;


    UserErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
