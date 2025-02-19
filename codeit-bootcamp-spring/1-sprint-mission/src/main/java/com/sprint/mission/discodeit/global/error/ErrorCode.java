package com.sprint.mission.discodeit.global.error;

public enum ErrorCode {

    // common
    NOT_FOUND(404, "요청한 데이터를 찾을 수 없습니다."),
    INVALID_SUBJECT_LENGTH(400, "채널 주제는 최대 1024자 이내여야 합니다."),
    // User
    INVALID_USERNAME_LENGTH(400, "유저 이름은 1~32자 이내여야 합니다."),
    INVALID_NICKNAME_LENGTH(400, "유저 이름은 2~32자 이내여야 합니다."),
    INVALID_PASSWORD_LENGTH(400, "비밀번호는 8~20자 이내여야 합니다."),
    WEAK_PASSWORD(400, "비밀번호는 대문자, 숫자, 특수문자를 포함해야 합니다."),
    UNDERAGE_SIGNUP_REGISTERED(400, "13세 이상만 회원가입이 가능합니다."),

    USERNAME_REQUIRED(400, "유저 이름은 필수입니다."),
    NICKNAME_REQUIRED(400, "닉네임은 필수입니다."),
    EMAIL_REQUIRED(400, "이메일은 필수입니다."),
    PASSWORD_REQUIRED(400, "비밀번호는 필수 입력값입니다."),
    BIRTHDATE_REQUIRED(400, "생년월일은 필수 입력값입니다."),

    INVALID_EMAIL_FORMAT(400, "이메일 형식이 올바르지 않습니다"),
    INVALID_USERNAME_FORMAT(400, "유저 이름에 허용되지 않은 문자가 포함되어 있습니다."),
    INVALID_NICKNAME_FORMAT(400, "닉네임에 허용되지 않은 문자가 포함되어 있습니다."),

    DUPLICATE_EMAIL(400, "이미 사용중인 이메일입니다."),
    DUPLICATE_USERNAME(400, "이미 존재하는 유저이름입니다."),

    INVALID_CREDENTIALS(400, "아이디 또는 비밀번호가 일치하지 않습니다."),

    // Channel
    INVALID_CHANNEL_NAME_NOT_NULL(400, "채널 이름은 필수 입력값 입니다."),
    ALREADY_CHANNEL_JOIN_USER(400, "이미 참여한 채널입니다."),
    CHANNEL_ADMIN_REQUIRED(400, "채널 삭제가 허용되지 않은 사용자입니다."),

    // Message
    INVALID_MESSAGE_CONTENT_NOT_NULL(400, "메시지 내용은 필수 입력값 입니다."),
    ;

    private final int status;
    private final String description;

    ErrorCode(int status, String description) {
        this.status = status;
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
