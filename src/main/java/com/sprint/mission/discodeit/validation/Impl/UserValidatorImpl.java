package com.sprint.mission.discodeit.validation.Impl;

import com.sprint.mission.discodeit.dto.UserRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserValidatorImpl implements UserValidator {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private final UserRepository userRepository;

    @Override
    public boolean isValidName(String name) {
        if (name.isBlank()) {
            log.error("The name is blank. name={}", name);
        } else if (name.length() < 2) {
            log.error("The name must be at least 2 length. name={}", name);
        } else if (userRepository.existsByName(name)) {
            log.error("The name already exists. name={}", name);
        }  else {
            return true;
        }
        return false;
    }

    @Override
    public boolean isValidEmail(String email) {
        if (!email.matches(EMAIL_REGEX)) {
            log.error("Email format does not match. email={}", email);
        } else if (userRepository.existsByEmail(email)) {
            log.error("The email already exists. email={}", email);
        } else {
            return true;
        }
        return false;
    }

    @Override
    public boolean isValidPassword(String password) {
        if (password.isBlank()) {
            log.error("The password is blank.");
        } else if (password.length() < 6) {
            log.error("The password must be at least 6 length");
        } else {
            return true;
        }
        return false;
    }
}
