package com.sprint.mission.discodeit.validator;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class UserValidator implements Validator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    public void validate(String name, String email) {
        validateName(name);
        validateEmail(email);
    }

    public void validateName(String name) {
        isBlank(name);
    }

    public void validateEmail(String email) {
        isBlank(email);
        checkEmailFormat(email);
    }

    public void checkEmailFormat(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("[ERROR] 잘못된 이메일 형식입니다.");
        }
    }
}
