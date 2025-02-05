package com.sprint.mission.discodeit.domain.user.validation;

import org.springframework.util.Assert;

public class NicknameValidator {
    private static final int MAX_LENGTH = 15;
    private static final int MIN_LENGTH = 1;
    private static final String LENGTH_ERROR_MESSAGE = "닉네임은 " + MIN_LENGTH + " ~ " + MAX_LENGTH + "제한입니다. : 입력한 이름 길이 = ";

    public static void validate(String value) {
        Assert.notNull(value, "닉네임은 필수입니다.");

        if (value.isBlank() || value.length() > MAX_LENGTH || value.length() < MIN_LENGTH) {
            throw new IllegalArgumentException(provideLengthNameErrorMessage(value));
        }
    }

    // TODO 에러 관리 따로 관리하기.
    private static String provideLengthNameErrorMessage(String value) {
        return LENGTH_ERROR_MESSAGE + value.length();
    }

}
