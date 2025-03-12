package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContentDto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.userDto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.dto.userDto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.duplication.DuplicateResourceException;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

  private final UserRepository userRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserMapper userMapper;

  public UserDto create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    if (userRepository.existsByUsername(userCreateRequest.username())) {
      throw new DuplicateResourceException(
          "Username already exists: " + userCreateRequest.username());
    }
    if (userRepository.existsByEmail(userCreateRequest.email())) {
      throw new DuplicateResourceException("Email already exists: " + userCreateRequest.email());
    }
    BinaryContent nullableProfile = optionalProfileCreateRequest
        .map(request -> {
          BinaryContent binaryContent = new BinaryContent(
              request.fileName(),
              (long) request.bytes().length,
              request.contentType()
          );
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), request.bytes());
          return binaryContent;
        })
        .orElse(null);
    User user = new User(userCreateRequest.username(), userCreateRequest.email(),
        userCreateRequest.password(), nullableProfile, Instant.now());
    userRepository.save(user);
    return userMapper.toDto(user);
  }

  @Transactional(readOnly = true)
  public UserDto findById(UUID userId) {
    return userRepository.findById(userId)
        .map(userMapper::toDto)
        .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
  }

  @Transactional(readOnly = true)
  public List<UserDto> findAll() {
    return userRepository.findAll().stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<User> findAllById(List<UUID> participantIds) {
    return userRepository.findAllById(participantIds);
  }

  @Transactional(readOnly = true)
  public User findByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new NoSuchElementException("User not found: " + username));
  }

  public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
    if (userRepository.existsByUsername(userUpdateRequest.newUsername())) {
      throw new DuplicateResourceException(
          "Username already exists: " + userUpdateRequest.newUsername());
    }
    if (userRepository.existsByEmail(userUpdateRequest.newEmail())) {
      throw new DuplicateResourceException("Email already exists: " + userUpdateRequest.newEmail());
    }
    if (user.getProfile() != null) {
      binaryContentRepository.delete(user.getProfile());
    }
    BinaryContent nullableProfile = optionalProfileCreateRequest
        .map(request -> {
          BinaryContent binaryContent = new BinaryContent(request.fileName(),
              (long) request.bytes().length, request.contentType());
          binaryContentStorage.put(binaryContent.getId(), request.bytes());
          return binaryContentRepository.save(binaryContent);
        })
        .orElse(null);

    user.update(userUpdateRequest.newUsername(), userUpdateRequest.newEmail(),
        userUpdateRequest.newPassword(),
        nullableProfile);
    userRepository.save(user);
    return userMapper.toDto(user);
  }

  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

    if (user.getProfile() != null) {
      binaryContentRepository.deleteById(user.getProfile().getId());
    }

    userStatusRepository.deleteById(user.getStatus().getId());

    userRepository.deleteById(userId);
  }
}
