package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserRequestDTO;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor //final 혹은 @NotNull이 붙은 필드의 생성자를 자동 생성하는 롬복 어노테이션

public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusService userStatusService;
  private final UserStatusRepository userStatusRepository;

  @Override
  public User createUser(UserCreateDTO userCreateDTO) {
    if (userRepository.existsByUsername(userCreateDTO.name())) {
      throw new IllegalArgumentException("이미 존재하는 이름입니다. ");
    }
    if (userRepository.existsByEmail(userCreateDTO.email())) {
      throw new IllegalArgumentException("이미 존재하는 이메일입니다. ");
    }

    User user = User.builder()
        .username(userCreateDTO.name())
        .email(userCreateDTO.email())
        .password(userCreateDTO.password())
        .profile(userCreateDTO.profile())
        .build();

    return userRepository.save(user);
  }

  @Override
  public UserRequestDTO findUserDTO(UUID userId) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new NoSuchElementException("user Not found"));
    UserStatus userStatus = userStatusRepository.findByUserId(userId);
    UserRequestDTO userRequestDTO = new UserRequestDTO(user, userStatus.isOnline());
    return userRequestDTO;
  }

  //내부 사용전용
  private User findbyId(UUID userId) {
    return userRepository.findById(userId).orElseThrow(
        () -> new NoSuchElementException("user Not found")
    );
  }

  private List<User> findAll() {
    return userRepository.findAll();
  }

  @Override
  public List<UserRequestDTO> findAllUserDTO() {
    List<User> userList = findAll();

    List<UserRequestDTO> userRequestDTOS = userList.stream()
        .map(user -> new UserRequestDTO(user, userStatusRepository.findByUserId(user.getId())
            .isOnline()))
        .collect(Collectors.toList());
    return userRequestDTOS;
  }

  @Override
  public User updateUser(UUID userID, UserUpdateDTO userUpdateDTO) {
    User user = findbyId(userID);
    user.updateUser(userUpdateDTO.newName(), userUpdateDTO.newEmail(), userUpdateDTO.newPassword());
    return userRepository.save(user);
  }

  @Override
  public void deleteUser(UUID userID) {
    userRepository.deleteById(userID);
  }

  @Override
  public UserStatusUpdateDTO updateUserStatus(UUID id,
      UserStatusUpdateDTO userUserStatusUpdateDTO) {
    UserStatusUpdateDTO userStatusUpdateDTO = new UserStatusUpdateDTO(
        userUserStatusUpdateDTO.time());
    userStatusService.updateByUserId(id, userStatusUpdateDTO);
    return userUserStatusUpdateDTO;
  }

}
