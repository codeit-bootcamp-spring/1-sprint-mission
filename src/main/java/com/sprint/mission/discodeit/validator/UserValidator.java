package com.sprint.mission.discodeit.validator;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.execption.user.UserAlreadyExistException;
import com.sprint.mission.discodeit.execption.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserValidator {

  private final UserRepository userRepository;

  public User validateUserExistsByUserId(UUID userId) {
    return userRepository.findUserById(userId)
        .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userId)));
  }

  public void validateDuplicateByName(String name) {
    if (userRepository.findUserByName(name).isPresent()) {
      throw new UserAlreadyExistException(Map.of("name", name));
    }

  }

  public void validateDuplicateUserByEmail(String email) {
    if (userRepository.findUserByEmail(email).isPresent()) {
      throw new UserAlreadyExistException(Map.of("email", email));
    }
  }

  public User validateUserExistsByNameAndPassword(String name, String password) {
    return userRepository.findUserByNameAndPassword(name, password)
        .orElseThrow(() -> {
          log.error("userNotFound");
          return new UserNotFoundException(Map.of("name", name, "password", password));
        });
  }
}
