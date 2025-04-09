package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.Map;
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
  public UserDto create(
      UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest
  ) {

    String username = userCreateRequest.username();
    String email = userCreateRequest.email();

    validateExistsEmail(email);
    validateExistsUsername(username);

    BinaryContent nullableProfile =
        optionalProfileCreateRequest.map(this::storeAndStoreProfileImage).orElse(null);

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
    User foundUser = getUserById(userId);
    return userMapper.toDto(foundUser);
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAllWithProfileAndStatus().stream().map(userMapper::toDto).toList();
  }

  @Transactional
  @Override
  public UserDto update(
      UUID userId,
      UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest
  ) {
    User user = getUserById(userId);

    String newUsername = userUpdateRequest.newUsername();
    String newEmail = userUpdateRequest.newEmail();
    validateExistsEmail(newEmail);
    validateExistsUsername(newUsername);

    BinaryContent nullableProfile =
        optionalProfileCreateRequest.map(this::storeAndStoreProfileImage).orElse(null);

    String newPassword = userUpdateRequest.newPassword();
    user.update(newUsername, newEmail, newPassword, nullableProfile);

    log.info("Updated user {}", user.getUsername());
    return userMapper.toDto(user);
  }

  @Transactional
  @Override
  public void delete(UUID userId) {
    if (userRepository.existsById(userId)) {
      throw UserNotFoundException.of(userId);
    }

    log.info("Deleted user {}", userId);
    userRepository.deleteById(userId);
  }

  private void validateExistsUsername(String username) {
    if (userRepository.existsByUsername(username)) {
      throw UserAlreadyExistsException.of(Map.of("User Name", username));
    }
  }

  private void validateExistsEmail(String email) {
    if (userRepository.existsByEmail(email)) {
      throw UserAlreadyExistsException.of(Map.of("Email", email));
    }
  }

  private BinaryContent storeAndStoreProfileImage(BinaryContentCreateRequest profileRequest) {
    String fileName = profileRequest.fileName();
    String contentType = profileRequest.contentType();
    byte[] bytes = profileRequest.bytes();
    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType);
    binaryContentRepository.save(binaryContent);
    binaryContentStorage.put(binaryContent.getId(), bytes);
    return binaryContent;
  }

  private User getUserById(UUID userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> UserNotFoundException.of(Map.of("User Id", userId)));
  }
}
