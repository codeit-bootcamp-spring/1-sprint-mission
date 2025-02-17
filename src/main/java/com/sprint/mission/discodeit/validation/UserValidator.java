package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.duplication.DuplicateResourceException;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.exception.validation.InvalidResourceException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.sprint.mission.discodeit.util.UserRegex.*;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    public void validateDuplicateUser(String username, String phoneNumber, String email){
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateResourceException("Username already exists: " + username);
        }

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email already exists: " + email);
        }

        if (userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DuplicateResourceException("Phone number already exists: " + phoneNumber);
        }
    }

    public void validateUser(String username, String phoneNumber, String email, String password) {
        if (!username.matches(USERNAME_REGEX)) {
            throw new InvalidResourceException("Invalid username: " + username);
        }

        if (!phoneNumber.matches(PHONE_NUMBER_REGEX)) {
            throw new InvalidResourceException("Invalid phoneNumber: " + email);
        }

        if (!email.matches(EMAIL_REGEX)) {
            throw new InvalidResourceException("Invalid email: " + email);
        }

        if (!password.matches(PASSWORD_REGEX)) {
            throw new InvalidResourceException("Invalid password");
        }
    }
}
