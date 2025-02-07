package com.sprint.mission.discodeit.validation.Impl;

import com.sprint.mission.discodeit.validation.UserValidator;
import org.springframework.stereotype.Component;

@Component
public class UserValidatorImpl implements UserValidator {
    private static final String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    @Override
    public boolean isValidName(String name) {
        if (name.isBlank()) {
            System.out.println("the name is blank");
        } else if (name.length() < 2) {
            System.out.println("the name is too short");
        } else {
            return true;
        }
        return false;
    }

    @Override
    public boolean isValidEmail(String email) {
        if (!email.matches(emailRegex)) {
            System.out.println("email format does not match");
        }
        return email.matches(emailRegex);
    }
}
