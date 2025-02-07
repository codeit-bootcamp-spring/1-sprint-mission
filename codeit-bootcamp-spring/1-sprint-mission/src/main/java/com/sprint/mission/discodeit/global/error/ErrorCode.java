package com.sprint.mission.discodeit.global.error;

public enum ErrorCode {


    // User Domain
    INVALID_USERNAME_LENGTH(400, "유저 이름은 1~32자 이내여야 합니다.", "U001"),
    INVALID_NICKNAME_LENGTH(400, "유저 이름은 2~32자 이내여야 합니다.", "U002"),
    INVALID_PASSWORD_LENGTH(400, "비밀번호는 8~20자 이내여야 합니다.", "U004"),  // 비밀번호 길이 오류
    WEAK_PASSWORD(400, "비밀번호는 대문자, 숫자, 특수문자를 포함해야 합니다.", "U008"),
    UNDERAGE_SIGNUP_REGISTERED(400, "13세 이상만 회원가입이 가능합니다.", "U009"),

    USERNAME_REQUIRED(400, "유저 이름은 필수입니다.", "U104"),
    NICKNAME_REQUIRED(400, "닉네임은 필수입니다.", "U105"),
    EMAIL_REQUIRED(400, "이메일은 필수입니다.", "U106"),
    PASSWORD_REQUIRED(400, "비밀번호는 필수 입력값입니다.", "U107"),
    BIRTHDATE_REQUIRED(400, "생년월일은 필수 입력값입니다.", "U108"),

    INVALID_EMAIL_FORMAT(400, "이메일 형식이 올바르지 않습니다", "U203"),
    INVALID_USERNAME_FORMAT(400, "유저 이름에 허용되지 않은 문자가 포함되어 있습니다.", "U205"),
    INVALID_NICKNAME_FORMAT(400, "닉네임에 허용되지 않은 문자가 포함되어 있습니다.", "U206"),

    DUPLICATE_EMAIL(400, "이미 사용중인 이메일입니다.", "U307"),
    DUPLICATE_USERNAME(400, "이미 존재하는 유저이름입니다.", "U308"),


    ;

    private final int status;
    private final String description;
    private final String code;

    ErrorCode(int status, String description, String code) {
        this.status = status;
        this.description = description;
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }
}
