package com.sprint.mission.discodeit.validation;

import static com.sprint.mission.discodeit.util.UserRegex.EMAIL_REGEX;
import static com.sprint.mission.discodeit.util.UserRegex.PASSWORD_REGEX;
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

  public void validateDuplicateUser(String username, String email) {
    if (userRepository.existsByUsername(username)) {
      throw new DuplicateResourceException("Username already exists: " + username);
    }

    if (userRepository.existsByEmail(email)) {
      throw new DuplicateResourceException("Email already exists: " + email);
    }
  }

  public void validateUser(String username, String email, String password) {
    if (!username.matches(USERNAME_REGEX)) {
      throw new InvalidResourceException("Invalid username: " + username);
    }

    if (!email.matches(EMAIL_REGEX)) {
      throw new InvalidResourceException("Invalid email: " + email);
    }

    if (!password.matches(PASSWORD_REGEX)) {
      throw new InvalidResourceException("Invalid password");
    }
  }
}
