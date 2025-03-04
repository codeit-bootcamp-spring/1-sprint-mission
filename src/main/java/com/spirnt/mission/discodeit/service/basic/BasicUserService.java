package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.dto.user.UserUpdateRequest;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.spirnt.mission.discodeit.enity.BinaryContent;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.enity.UserStatus;
import com.spirnt.mission.discodeit.enity.UserStatusType;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.BinaryContentService;
import com.spirnt.mission.discodeit.service.UserService;
import com.spirnt.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;

  private final BinaryContentService binaryContentService;
  private final UserStatusService userStatusService;

  @Override
  public User create(UserCreateRequest userCreateRequest,
      BinaryContentCreateRequest binaryContentCreateRequest) {
    // 파라미터 검증
    if (checkEmailDuplicate(userCreateRequest.getEmail())) {
      throw new IllegalArgumentException(
          "User with email " + userCreateRequest.getEmail() + " already exists");
    }
    if (checkNameDuplicate(userCreateRequest.getUsername())) {
      throw new IllegalArgumentException(
          "User with name " + userCreateRequest.getUsername() + " already exists");
    }
    // 프로필 이미지 저장
    BinaryContent profile = (binaryContentCreateRequest != null) ? binaryContentService.create(
        binaryContentCreateRequest) : null;
    UUID profileImageId = (profile == null) ? null : profile.getId();

    // User 생성, 저장
    User user = new User(userCreateRequest.getUsername(),
        userCreateRequest.getEmail(),
        userCreateRequest.getPassword(),
        profileImageId);
    userRepository.save(user);
    // UserStatus 생성
    UserStatusCreateRequest userStatusCreateRequest = new UserStatusCreateRequest(user.getId(),
        UserStatusType.ONLINE,
        Instant.now());
    userStatusService.create(userStatusCreateRequest);

    return user;
  }

  @Override
  public UserDto find(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id" + userId + " not found"));
    UserStatus userStatus = userStatusService.findByUserId(userId);
    return new UserDto(user, userStatus);
  }

  @Override
  public List<UserDto> findAll() {
    Map<UUID, User> data = userRepository.findAll();
    return data.values().stream()
        .sorted(Comparator.comparing(user -> user.getCreatedAt()))
        .map(user -> find(user.getId()))
        .collect(Collectors.toList());
  }

  @Override
  public User update(UUID userId, UserUpdateRequest userUpdateRequest,
      BinaryContentCreateRequest binaryContentCreateRequest) {
    // User 객체 변경
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id" + userId + " not found"));
    if (checkEmailDuplicate(userUpdateRequest.getNewEmail())) {
      throw new IllegalArgumentException(
          "User with email " + userUpdateRequest.getNewEmail() + " already exists");
    }
    if (checkNameDuplicate(userUpdateRequest.getNewUsername())) {
      throw new IllegalArgumentException(
          "User with name " + userUpdateRequest.getNewUsername() + " already exists");
    }
    // 프로필 이미지 저장
    BinaryContent profile = (binaryContentCreateRequest != null) ? binaryContentService.create(
        binaryContentCreateRequest) : null;
    UUID profileImageId = (profile == null) ? null : profile.getId();
    user.update(userUpdateRequest.getNewUsername(), userUpdateRequest.getNewEmail(),
        userUpdateRequest.getNewPassword(), profileImageId);
    userRepository.save(user);
    return user;
  }

  @Override
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id" + userId + " not found"));
    // 유저의 프로필  BinaryContent 삭제
    binaryContentService.delete(user.getProfileId());
    // UserStatus 삭제
    userStatusService.deleteByUserId(userId);
    userRepository.delete(userId);
  }


  public boolean checkEmailDuplicate(String email) {
    Map<UUID, User> users = userRepository.findAll();
    if (users == null || users.isEmpty()) {
      return false;
    }
    return users.values().stream()
        .anyMatch(user -> user.getEmail().equals(email));
  }

  public boolean checkNameDuplicate(String name) {
    Map<UUID, User> users = userRepository.findAll();
    if (users == null || users.isEmpty()) {
      return false;
    }
    return users.values().stream()
        .anyMatch(user -> user.getUsername().equals(name));
  }

}
