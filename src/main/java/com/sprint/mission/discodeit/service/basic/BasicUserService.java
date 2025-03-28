package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserMapper userMapper;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  @Override
  public UserDto create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    String username = userCreateRequest.username();
    String email = userCreateRequest.email();

    if (isExistsByEmail(email)) {
      throw new IllegalArgumentException("User with email " + email + " already exists");
    }
    if (isExistsByUsername(username)) {
      throw new IllegalArgumentException("User with username " + username + " already exists");
    }

    BinaryContent nullableProfile = optionalProfileCreateRequest.map(profileRequest -> {
      String fileName = profileRequest.fileName();
      String contentType = profileRequest.contentType();
      byte[] bytes = profileRequest.bytes();
      BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType);
      binaryContentRepository.save(binaryContent);
      binaryContentStorage.put(binaryContent.getId(), bytes);
      return binaryContent;
    }).orElse(null);
    String password = userCreateRequest.password();

    User user = new User(username, email, password, nullableProfile);
    Instant now = Instant.now();
    UserStatus userStatus = new UserStatus(user, now);

    userRepository.save(user);
    log.info("Created user {}", user.getUsername());
    return userMapper.toDto(user);
  }


  @Override
  public UserDto find(UUID userId) {
    return userRepository.findById(userId).map(userMapper::toDto)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAllWithProfileAndStatus().stream().map(userMapper::toDto).toList();
  }

  @Transactional
  @Override
  public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

    String newUsername = userUpdateRequest.newUsername();
    String newEmail = userUpdateRequest.newEmail();
    if (isExistsByEmail(newEmail)) {
      throw new IllegalArgumentException("User with email " + newEmail + " already exists");
    }
    if (isExistsByUsername(newUsername)) {
      throw new IllegalArgumentException("User with username " + newUsername + " already exists");
    }

    BinaryContent nullableProfile = optionalProfileCreateRequest.map(profileRequest -> {

      String fileName = profileRequest.fileName();
      String contentType = profileRequest.contentType();
      byte[] bytes = profileRequest.bytes();
      BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType);
      binaryContentRepository.save(binaryContent);
      binaryContentStorage.put(binaryContent.getId(), bytes);
      return binaryContent;
    }).orElse(null);

    String newPassword = userUpdateRequest.newPassword();
    user.update(newUsername, newEmail, newPassword, nullableProfile);
    log.info("Updated user {}", user.getUsername());
    return userMapper.toDto(user);
  }
  @Transactional
  @Override
  public void delete(UUID userId) {
    if (userRepository.existsById(userId)) {
      throw new NoSuchElementException("User with id " + userId + " not found");
    }
    log.info("Deleted user {}", userId);
    userRepository.deleteById(userId);
  }

  private boolean isExistsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  private boolean isExistsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }
}
