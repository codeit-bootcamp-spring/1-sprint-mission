package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionText;

import java.util.regex.Pattern;

public class UserValidator {

    // 간단한 형태의 이메일 검증
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    // 비밀번호 검증: 최소 8자, 대문자, 소문자, 숫자, 특수문자 포함
    private static final String PASSWORD_REGEX = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,}";
    private static final int MIN_NAME_LENGTH = 2;

    public static boolean isValidEmail(String email) {
        if (email == null || !Pattern.matches(EMAIL_REGEX, email)) {
            throw new CustomException(ExceptionText.INVALID_EMAIL);
        }
        return true;
    }

    public static boolean isValidPassword(String password) {
        if (password == null || !Pattern.matches(PASSWORD_REGEX, password)) {
            throw new CustomException(ExceptionText.INVALID_PASSWORD);
        }
        return true;
    }

    public static boolean isValidName(String name) {
        if (name == null || name.length() < MIN_NAME_LENGTH) {
            throw new CustomException(ExceptionText.INVALID_NAME);
        }
        return true;
    }

    public static boolean validateUser(String name, String email, String password) {
        return isValidName(name) && isValidEmail(email) && isValidPassword(password);
    }
}
