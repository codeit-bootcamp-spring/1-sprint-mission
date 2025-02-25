package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  //
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  public User create(UserCreateRequest userCreateRequest,
      BinaryContentCreateRequest profileCreateRequest) {
    String username = userCreateRequest.username();
    String email = userCreateRequest.email();

    validateUserExistByEmail(email);
    validateUserExistByUsername(username);

    UUID profileId = getProfileId(profileCreateRequest);
    String password = userCreateRequest.password();

    User user = User.createUser(username, email, password, profileId);
    User createdUser = userRepository.save(user);

    Instant now = Instant.now();
    UserStatus userStatus = UserStatus.createUserStatus(createdUser.getId(), now);
    userStatusRepository.save(userStatus);

    return createdUser;
  }

  private UUID getProfileId(BinaryContentCreateRequest profileCreateRequest) {
    UUID profileId = null;
    if (!profileCreateRequest.isEmpty()) {

      BinaryContent binaryContent = getBinaryContent(profileCreateRequest);
      profileId = binaryContentRepository.save(binaryContent).getId();
    }
    return profileId;
  }

  private void validateUserExistByUsername(String username) {
    if (userRepository.existsByUsername(username)) {
      throw new UserAlreadyExistException(
          "User with username " + username + " already exists");
    }
  }

  private void validateUserExistByEmail(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new UserAlreadyExistException("User with email " + email + " already exists");
    }
  }

  private static BinaryContent getBinaryContent(BinaryContentCreateRequest profileCreateRequest) {
    String fileName = profileCreateRequest.fileName();
    String contentType = profileCreateRequest.contentType();
    byte[] bytes = profileCreateRequest.bytes();
    return BinaryContent.createBinaryContent(
        fileName, (long) bytes.length, contentType, bytes);
  }

  @Override
  public UserDto find(UUID userId) {
    return userRepository.findById(userId)
        .map(this::toDto)
        .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAll()
        .stream()
        .map(this::toDto)
        .toList();
  }

  @Override
  public User update(UUID userId, UserUpdateRequest userUpdateRequest,
      BinaryContentCreateRequest profileCreateRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

    String newUsername = userUpdateRequest.username();
    String newEmail = userUpdateRequest.email();
    validateUserExistByEmail(newEmail);
    validateUserExistByUsername(newUsername);

    UUID profileId = user.getProfileId();
    if (!profileCreateRequest.isEmpty()) {
      if (profileId != null) {
        binaryContentRepository.deleteById(profileId);
      }

      BinaryContent binaryContent = getBinaryContent(profileCreateRequest);
      profileId = binaryContentRepository.save(binaryContent).getId();
    }

    String newPassword = userUpdateRequest.password();
    User updatedUser = user.update(newUsername, newEmail, newPassword, profileId);

    return userRepository.save(updatedUser);
  }

  @Override
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

    Optional.ofNullable(user.getProfileId())
        .ifPresent(binaryContentRepository::deleteById);
    userStatusRepository.deleteByUserId(userId);

    userRepository.deleteById(userId);
  }

  private UserDto toDto(User user) {
    Boolean online = userStatusRepository.findByUserId(user.getId())
        .map(UserStatus::isOnline)
        .orElse(null);

    return new UserDto(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        user.getProfileId(),
        online
    );
  }
}
