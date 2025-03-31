package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
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
@Service
@RequiredArgsConstructor  // 사용 시 필수 필드에 private final 필수
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserMapper userMapper;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  @Transactional
  public UserDto create(CreateUserRequest userRequest,
      Optional<CreateBinaryContentRequest> profileRequest) {
    String username = userRequest.username();
    String email = userRequest.email();
    String password = userRequest.password();

    if (userRepository.existsByEmail(email)) {
      log.error("Email duplication : email={}", email);
      throw new IllegalArgumentException("User with email " + email + " already exists");
    }
    if (userRepository.existsByUsername(username)) {
      log.error("Username duplication : username={}", username);
      throw new IllegalArgumentException("User with username " + username + " already exists");
    }

    BinaryContent profile = convertToBinaryContent(profileRequest);

    User user = new User(username, email, password, profile);
    Instant now = Instant.now();
    UserStatus userStatus = new UserStatus(user, now);

    userStatusRepository.save(userStatus);
    userRepository.save(user);
    return userMapper.toDto(user);
  }

  @Override
  public UserDto find(UUID userId) {
    return userRepository.findById(userId)
        .map(userMapper::toDto)
        .orElseThrow(
            () -> new NoSuchElementException("User with id " + userId + " not found")
        );
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAllWithProfileAndStatus().stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public UserDto update(UUID userId, UpdateUserRequest userRequest,
      Optional<CreateBinaryContentRequest> profileRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(
            () -> {
              log.error("User not found : userId={}", userId);
              return new NoSuchElementException("User with id " + userId + " not found");
            }
        );

    String newUsername = userRequest.newUsername();
    String newEmail = userRequest.newEmail();
    String newPassword = userRequest.newPassword();

    if (userRepository.existsByEmail(newEmail)) {
      log.error("Email duplication : newEmail={}", newEmail);
      throw new IllegalArgumentException("User with email " + newEmail + " already exists");
    }
    if (userRepository.existsByUsername(newUsername)) {
      log.error("username duplication : newUsername={}", newUsername);
      throw new IllegalArgumentException("User with username " + newUsername + " already exists");
    }

    BinaryContent profile = convertToBinaryContent(profileRequest);

    user.update(newUsername, newEmail, newPassword, profile);

    return userMapper.toDto(user);
  }

  @Override
  @Transactional
  public void delete(UUID userId) {
    if (!userRepository.existsById(userId)) {
      log.error("User not found : userId={}", userId);
      throw new NoSuchElementException("User with id " + userId + " not found");
    }

    userRepository.deleteById(userId);
  }


  // Optional<CreateBinaryContentRequest> -> BinaryContent
  private BinaryContent convertToBinaryContent(
      Optional<CreateBinaryContentRequest> binaryContentRequest) {
    return binaryContentRequest
        .map(request -> {
          String fileName = request.fileName();
          String contentType = request.contentType();
          byte[] bytes = request.bytes();
          BinaryContent binaryContent = new BinaryContent(
              fileName,
              (long) bytes.length,
              contentType
          );
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          return binaryContent;
        })
        .orElse(null);
  }
}