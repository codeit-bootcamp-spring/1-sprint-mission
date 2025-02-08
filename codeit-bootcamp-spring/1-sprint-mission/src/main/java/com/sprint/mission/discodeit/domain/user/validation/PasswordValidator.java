package com.sprint.mission.discodeit.domain.user.validation;

import com.sprint.mission.discodeit.domain.user.exception.PassWordInvalidException;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import java.util.Objects;
import java.util.regex.Pattern;

public class PasswordValidator {
    private final static int MAX_PASSWORD_LENGTH = 100;
    private final static int MIN_PASSWORD_LENGTH = 8;
    private final static String PASSWORD_REX = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*()_+])\\S{8,}$";
    private final static Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REX);

    public static void validateOrThrow(String password) {
        if (Objects.isNull(password) || password.isEmpty()) {
            throw new PassWordInvalidException(ErrorCode.PASSWORD_REQUIRED, "");
        }
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            throw new PassWordInvalidException(ErrorCode.INVALID_PASSWORD_LENGTH, password);
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new PassWordInvalidException(ErrorCode.WEAK_PASSWORD, password);
        }
    }
}
