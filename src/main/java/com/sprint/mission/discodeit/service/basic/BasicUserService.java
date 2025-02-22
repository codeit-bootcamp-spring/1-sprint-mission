package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.PasswordEncryptor;
import com.sprint.mission.discodeit.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sprint.mission.discodeit.constant.UserConstant.*;

@Slf4j
@Service
@ConditionalOnProperty(name = "app.service.type", havingValue = "basic")
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;

  private final EntityValidator validator;

  @Override
  public User saveUser(User user) {

    validateUserInformationWhenCreate(user, user.getId());

    return userRepository.create(user);
  }

  @Override
  public User update(User user){
    return userRepository.update(user);
  }

  private void validateUserInformationWhenCreate(User user, String userId) {
    List<User> users = userRepository.findAll();
    validEmail(user.getEmail(), userId, users);

  }

  private void validEmail(String email, String id, List<User> users) {
    if (!email.matches(EMAIL_REGEX)) throw new UserValidationException(ERROR_INVALID_EMAIL);
    if (users.stream()
        .anyMatch(u -> u.getEmail().equals(email) && !u.getId().equals(id)))
      throw new UserValidationException(DUPLICATE_EMAIL);
  }


  @Override
  public User findUserById(String id) {
    return validator.findOrThrow(User.class, id, new UserNotFoundException());
  }

  @Override
  public List<User> findAllUsers() {
    return userRepository.findAll();
  }


  @Override
  public User updateUser(String id, UserUpdateDto updatedUser) {

    User originalUser = validator.findOrThrow(User.class, id, new UserNotFoundException());

    synchronized (originalUser) {
      updateFields(originalUser, updatedUser);
    }

    return userRepository.update(originalUser);
  }

  private void updateFields(User originalUser, UserUpdateDto updatedUser) {
    List<User> users = userRepository.findAll();

    if (updatedUser.newEmail() != null) {
      validEmail(updatedUser.newEmail(), originalUser.getId(), users);
    }

    originalUser.updateProfile(
        updatedUser.newUsername(),
        updatedUser.newEmail(),
        updatedUser.newPassword() == null ? null : PasswordEncryptor.hashPassword(updatedUser.newPassword())
    );

  }

  @Override
  public void deleteUser(String id) {
    validator.findOrThrow(User.class, id, new UserNotFoundException());

    userRepository.delete(id);
  }
}
