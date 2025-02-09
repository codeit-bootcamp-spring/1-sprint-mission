package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.exception.duplication.DuplicateResourceException;
import com.sprint.mission.discodeit.exception.validation.InvalidResourceException;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidateUser {
    private final UserRepository userRepository;
    private static final String EMAIL_REGEX = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$";
    private static final String PHONE_NUMBER_REGEX = "^\\d{2,3}\\d{3,4}\\d{4}$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$";
    private static final String USERNAME_REGEX = "^.{1,32}$";

    public void validateUser(String username, String email, String phoneNumber, String password){
        validateUsername(username);
        validateEmail(email);
        validatePhoneNumber(phoneNumber);
        validatePassword(password);
        validateDuplicateUsername(username);
        validateDuplicateEmail(email);
    }

    public void validateDuplicateUsername(String username){
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateResourceException("Username already exists.");
        }
    }

    public void validateDuplicateEmail(String email){
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email already exists.");
        }
    }

    public void validateUsername(String username) {
        if (username == null || !username.matches(USERNAME_REGEX)) {
            throw new InvalidResourceException("Invalid username.");
        }
    }

    public static void validateEmail(String email) {
        if (email == null || !email.matches(EMAIL_REGEX)) {
            throw new InvalidResourceException("Invalid email.");
        }
    }

    public static void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches(PHONE_NUMBER_REGEX)) {
            throw new InvalidResourceException("Invalid phone number.");
        }
    }

    public static void validatePassword(String password) {
        if (password == null || !password.matches(PASSWORD_REGEX)) {
            throw new InvalidResourceException("Invalid password.");
        }
    }

}
