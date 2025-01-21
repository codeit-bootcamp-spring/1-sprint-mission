package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.constant.UserConstant;
import com.sprint.mission.discodeit.dto.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.PasswordEncryptor;

import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.UserConstant.*;

public class BasicUserService implements UserService {

  private static volatile BasicUserService instance;
  private final UserRepository userRepository;

  private BasicUserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public static BasicUserService getInstance(UserRepository userRepository) {
    if (instance == null) {
      synchronized (BasicUserService.class) {
        if (instance == null) {
          instance = new BasicUserService(userRepository);
        }
      }
    }
    return instance;
  }

  @Override
  public User createUser(User user) {
    validEmail(user.getEmail());
    validNickname(user.getNickname());
    user.setPassword(PasswordEncryptor.hashPassword(user.getPassword()));
    userRepository.create(user);
    return user;
  }

  private void validEmail(String email) {
    if (!email.matches(EMAIL_REGEX)) throw new UserValidationException(ERROR_INVALID_EMAIL);
    List<User> users = userRepository.findAll();
    if (users.stream()
        .anyMatch(u -> u.getEmail().equals(email))) throw new UserValidationException(DUPLICATE_EMAIL);

  }

  private void validPhone(String phoneNumber) {
    if (!phoneNumber.matches(PHONE_REGEX)) throw new UserValidationException(ERROR_INVALID_PHONE);

    List<User> users = userRepository.findAll();

    if (users.stream()
        .anyMatch(u -> u.getPhoneNumber().equals(phoneNumber))) throw new UserValidationException(DUPLICATE_PHONE);
  }

  private void validNickname(String nickname) {
    if (nickname.length() <= UserConstant.USERNAME_MIN_LENGTH
        || nickname.length() > UserConstant.USERNAME_MAX_LENGTH) {
      throw new UserValidationException(UserConstant.ERROR_USERNAME_LENGTH);
    }
  }

  @Override
  public Optional<User> readUserById(String id) {
    return userRepository.findById(id);
  }

  @Override
  public List<User> readAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public void updateUser(String id, UserUpdateDto updatedUser, String originalPassword) {
    User originalUser = userRepository.findById(id).get();

    if(!PasswordEncryptor.checkPassword(originalPassword, originalUser.getPassword())) throw new UserValidationException(PASSWORD_MATCH_ERROR);

    updatedUser.getNickname().ifPresent(nickname -> {
      validNickname(nickname);
      originalUser.setNickname(nickname);
    });
    updatedUser.getEmail().ifPresent(email -> {
      validEmail(email);
      originalUser.setEmail(email);
    });
    updatedUser.getPhoneNumber().ifPresent(phone -> {
      validPhone(phone);
      originalUser.setPhoneNumber(phone);
    });
    updatedUser.getUsername().ifPresent(originalUser::setUsername);
    updatedUser.getDescription().ifPresent(originalUser::setDescription);
    updatedUser.getProfilePictureURL().ifPresent(originalUser::setProfilePictureURL);

    userRepository.update(originalUser);
  }

  @Override
  public void deleteUser(String id, String password) {
    User user = userRepository.findById(id).get();
    if(!PasswordEncryptor.checkPassword(password, user.getPassword())) throw new UserValidationException(PASSWORD_MATCH_ERROR);
    userRepository.delete(id);
  }
}
