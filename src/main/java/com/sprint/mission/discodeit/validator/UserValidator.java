package com.sprint.mission.discodeit.validator;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.error.execption.user.UserAlreadyExistException;
import com.sprint.mission.discodeit.global.error.execption.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

  private final UserRepository userRepository;

  public User validateUserExistsByUserId(UUID userId) {
    return userRepository.findUserById(userId)
        .orElseThrow(() -> new UserNotFoundException("id: " + userId));
  }

  public void validateDuplicateByName(String name) {
    if (userRepository.findUserByName(name).isPresent()) {
      throw new UserAlreadyExistException("name: " + name);
    }

  }

  public void validateDuplicateUserByEmail(String email) {
    if (userRepository.findUserByEmail(email).isPresent()) {
      throw new UserAlreadyExistException("email: " + email);
    }
  }

  public User validateUserExistsByNameAndPassword(String name, String password) {
    return userRepository.findUserByNameAndPassword(name, password)
        .orElseThrow(() -> new UserNotFoundException("name: " + name + " password: " + password));
  }

}
