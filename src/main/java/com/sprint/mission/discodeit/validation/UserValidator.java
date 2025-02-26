package com.sprint.mission.discodeit.validation;

import static com.sprint.mission.discodeit.util.UserRegex.EMAIL_REGEX;
import static com.sprint.mission.discodeit.util.UserRegex.PASSWORD_REGEX;
import static com.sprint.mission.discodeit.util.UserRegex.PHONE_NUMBER_REGEX;
import static com.sprint.mission.discodeit.util.UserRegex.USERNAME_REGEX;

import com.sprint.mission.discodeit.exception.duplication.DuplicateResourceException;
import com.sprint.mission.discodeit.exception.validation.InvalidResourceException;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

  private final UserRepository userRepository;

  public void validateDuplicateUser(String username, String phoneNumber, String email) {
    if (userRepository.existsByUsername(username)) {
      throw new DuplicateResourceException("Username already exists: " + username);
    }

    if (userRepository.existsByEmail(email)) {
      throw new DuplicateResourceException("Email already exists: " + email);
    }

    if (userRepository.existsByPhoneNumber(phoneNumber)) {
      throw new DuplicateResourceException("Phone number already exists: " + phoneNumber);
    }
  }

  public void validateUser(String username, String phoneNumber, String email, String password) {
    if (!username.matches(USERNAME_REGEX)) {
      throw new InvalidResourceException("Invalid username: " + username);
    }

    if (!phoneNumber.matches(PHONE_NUMBER_REGEX)) {
      throw new InvalidResourceException("Invalid phoneNumber: " + phoneNumber);
    }

    if (!email.matches(EMAIL_REGEX)) {
      throw new InvalidResourceException("Invalid email: " + email);
    }

    if (!password.matches(PASSWORD_REGEX)) {
      throw new InvalidResourceException("Invalid password");
    }
  }
}
