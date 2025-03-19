package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  //
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final UserStatusRepository userStatusRepository;

  @Override
  public UserDto create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> optionalBinaryContentDto) {
    String username = userCreateRequest.username();
    String email = userCreateRequest.email();

    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("User with email " + email + " already exists");
    }
    if (userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("User with username " + username + " already exists");
    }

    BinaryContent nullableBinaryContent = optionalBinaryContentDto
        .map(profileRequest -> {
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          Long size = profileRequest.size();
          BinaryContent binaryContent = new BinaryContent(fileName, size,
              contentType);
          byte[] bytes = profileRequest.bytes();
          binaryContentStorage.put(binaryContent.getId(), bytes);
          return binaryContentRepository.save(binaryContent);
        })
        .orElse(null);
    String password = userCreateRequest.password();

    User user = new User(username, email, password, nullableBinaryContent);
    User createdUser = userRepository.save(user);

    Instant now = Instant.now();
    UserStatus userStatus = new UserStatus(createdUser, now);
    userStatusRepository.save(userStatus);
    user.setUserStatus(userStatus);
    Optional<User> byId = userRepository.findById(createdUser.getId());
    byId.ifPresent(value -> System.out.println("byId.get() = " + value));
    return new UserDto(createdUser);
  }

  @Override
  public UserDto find(UUID userId) {
    return userRepository.findById(userId)
        .map(this::toDto)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
  }

  @Override
  public List<UserDto> findAll() {
    List<User> users = userRepository.findAll();
    for (User user : users) {
      System.out.println("user = " + user);
    }
    List<UserDto> list = users.stream().map(this::toDto).toList();
    for (UserDto userDto : list) {
      System.out.println("userDto = " + userDto);
    }
    return list;
  }

  @Override
  public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    String newUsername = userUpdateRequest.newUsername();
    String newEmail = userUpdateRequest.newEmail();
    if (userRepository.existsByEmail(newEmail)) {
      throw new IllegalArgumentException("User with email " + newEmail + " already exists");
    }
    if (userRepository.existsByUsername(newUsername)) {
      throw new IllegalArgumentException("User with username " + newUsername + " already exists");
    }

    BinaryContent nullableBinaryContent = optionalProfileCreateRequest.map(profileRequest -> {
          Optional.ofNullable(user.getProfile().getId()).ifPresent(binaryContentRepository::deleteById);

          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          Long size = profileRequest.size();
          BinaryContent binaryContent = new BinaryContent(fileName, size, contentType);

          byte[] bytes = profileRequest.bytes();
          binaryContentStorage.put(binaryContent.getId(), bytes);
          return binaryContentRepository.save(binaryContent);
        })
        .orElse(null);

    String newPassword = userUpdateRequest.newPassword();
    user.update(newUsername, newEmail, newPassword, nullableBinaryContent);
    User save = userRepository.save(user);
    return new UserDto(save);
  }

  @Override
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    Optional.ofNullable(user.getProfile().getId()).ifPresent(binaryContentRepository::deleteById);
    userStatusRepository.deleteByUserId(userId);
    userRepository.deleteById(userId);
  }

  private UserDto toDto(User user) {
    //Boolean online = userStatusRepository.findByUserId(user.getId()).map(UserStatus::isOnline).orElse(false);

    return new UserDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getProfile(),
        user.getUserStatus().isOnline()
    );
  }
}
