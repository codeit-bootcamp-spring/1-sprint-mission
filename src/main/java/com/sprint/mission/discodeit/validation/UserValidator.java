package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionText;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public class UserValidator {

    // 간단한 형태의 이메일 검증
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    // 비밀번호 검증: 최소 8자, 대문자, 소문자, 숫자, 특수문자 포함
    private static final String PASSWORD_REGEX = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,}";
    private static final int MIN_NAME_LENGTH = 2;

    private boolean isValidEmail(String email) {
        if (email == null || !Pattern.matches(EMAIL_REGEX, email)) {
            throw new CustomException(ExceptionText.INVALID_EMAIL);
        }
        return true;
    }

    private boolean isValidPassword(String password) {
        if (password == null || !Pattern.matches(PASSWORD_REGEX, password)) {
            throw new CustomException(ExceptionText.INVALID_PASSWORD);
        }
        return true;
    }

    private boolean isValidName(String name) {
        if (name == null || name.length() < MIN_NAME_LENGTH) {
            throw new CustomException(ExceptionText.INVALID_NAME);
        }
        return true;
    }

    private boolean isUniqueName(String name, Map<UUID, User> users) {
        if (users.values().stream().anyMatch(user -> user.getName().equals(name))) {
            throw new CustomException(ExceptionText.DUPLICATE_NAME);
        }
        return true;// 중복 이름 예외
    }



    public boolean validateUser(String name, String email, String password, Map<UUID, User> users) {
        return isValidName(name) && isValidEmail(email) && isValidPassword(password) && isUniqueName(name, users);
    }
}
