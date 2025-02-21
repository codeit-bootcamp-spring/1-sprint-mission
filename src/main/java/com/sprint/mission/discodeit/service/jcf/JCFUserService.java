/*
package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.PasswordEncryptor;

import java.time.Instant;
import java.util.*;

public class JCFUserService implements UserService {

  private static volatile JCFUserService userRepository;

  // TODO : private 으로 변경
  private final Map<String, User> data = new HashMap<>();


  private JCFUserService() {
  }

  public static JCFUserService getInstance() {
    if (userRepository == null) {
      synchronized (JCFUserService.class) {
        if (userRepository == null) {
          userRepository = new JCFUserService();
        }
      }
    }
    return userRepository;
  }


  @Override
  public User createUser(CreateUserDto userDto) {
    if (!checkEmailDuplicate(userDto.email())) throw new IllegalArgumentException("중복된 이메일 입니다.");
    if (!checkPhoneNumberDuplicate(userDto.phoneNumber())) throw new IllegalArgumentException("중복된 전화번호 입니다.");

    User user = new User.UserBuilder(
        userDto.username(),
        PasswordEncryptor.hashPassword(userDto.password()),
        userDto.email(),
        userDto.phoneNumber()
    )
        .nickname(userDto.nickname())
        .binaryContentId(userDto.binaryContentId())
        .description(userDto.description())
        .build();



    data.put(user.getId(), user);

    return user;
  }

  @Override
  public UserResponseDto findUserById(String id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public List<UserResponseDto> findAllUsers() {
    return Collections.unmodifiableList(new ArrayList<>(data.values()));
  }

  @Override
  public void updateUser(String id, UserUpdateDto updatedUser, String originalPassword) {

    if (!PasswordEncryptor.checkPassword(originalPassword, data.get(id).getPassword())) {
      throw new IllegalArgumentException("비밀번호가 틀립니다.");
    }
    User originalUser = data.get(id);
    synchronized (originalUser) {
      updatedUser.getUsername().ifPresent(originalUser::setUsername);
      updatedUser.getPassword().map(PasswordEncryptor::hashPassword).ifPresent(originalUser::setPassword);
      updatedUser.getEmail().ifPresent(originalUser::setEmail);
      updatedUser.getNickname().ifPresent(originalUser::setNickname);
      updatedUser.getPhoneNumber().ifPresent(originalUser::setPhoneNumber);
      updatedUser.getProfileBinaryContentId().ifPresent(originalUser::setBinaryContentId);
      updatedUser.getDescription().ifPresent(originalUser::setDescription);

      originalUser.setUpdatedAt(Instant.now());
    }
    data.put(id, originalUser);
  }

  @Override
  public void deleteUser(String id, String password) {
    // TODO : 존재하지 않는 ID 예외
    if (PasswordEncryptor.checkPassword(password, data.get(id).getPassword())) {
      data.remove(id);
    } else {
      throw new IllegalArgumentException("비밀번호가 틀립니다.");
    }
  }

  private boolean checkEmailDuplicate(String email) {
    for (User currentUser : data.values()) {
      if (currentUser.getEmail().equals(email)) return false;
    }

    return true;
  }

  private boolean checkPhoneNumberDuplicate(String phoneNumber) {
    for (User currentUser : data.values()) {
      if (currentUser.getPhoneNumber().equals(phoneNumber)) return false;
    }
    return true;
  }
}
*/
