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
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserMapper userMapper;
  private final UserStatusRepository userStatusRepository;

  @Transactional
  @Override
  public UserDto create(UserCreateRequest request,
      Optional<BinaryContentCreateRequest> profileRequest) {
    if (userRepository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("User with email " + request.email() + " already exists");
    }
    if (userRepository.existsByUsername(request.username())) {
      throw new IllegalArgumentException(
          "User with username " + request.username() + " already exists");
    }
    BinaryContent profile = profileRequest.map(this::createProfile).orElse(null);
    User user = new User(request.username(), request.email(), request.password(), profile);
    user = userRepository.save(user);

    UserStatus userStatus = new UserStatus(user, Instant.now());
    userStatusRepository.save(userStatus);

    return userMapper.toDto(user);
  }

  @Override
  public UserDto find(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(
            () -> new NoSuchElementException("User with id " + userId + " not found"));

    return userMapper.toDto(user);
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAll()
        .stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public UserDto update(UUID userId, UserUpdateRequest request,
      Optional<BinaryContentCreateRequest> profileRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

    if (userRepository.existsByEmail(request.newEmail()) && !user.getEmail()
        .equals(request.newEmail())) {
      throw new IllegalArgumentException(
          "User with email " + request.newEmail() + " already exists");
    }
    if (userRepository.existsByUsername(request.newUsername()) && !user.getUsername()
        .equals(request.newUsername())) {
      throw new IllegalArgumentException(
          "User with username " + request.newUsername() + " already exists");
    }
    BinaryContent newProfile = user.getProfile();
    if (profileRequest.isPresent()) {
      profileRequest.ifPresent(binaryContentCreateRequest -> {
        if (user.getProfile() != null) {
          binaryContentRepository.delete(user.getProfile());
        }
        user.setProfile(createProfile(binaryContentCreateRequest));
      });
    }

    user.update(request.newUsername(), request.newEmail(), request.newPassword(),
        newProfile);
    return userMapper.toDto(user);
  }

  @Transactional
  @Override
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

    Optional.ofNullable(user.getProfile())
        .ifPresent(binaryContentRepository::delete);
    userRepository.deleteById(userId);
  }

  private BinaryContent createProfile(BinaryContentCreateRequest profileRequest) {
    return new BinaryContent(
        UUID.randomUUID(),
        profileRequest.fileName(),
        (long) profileRequest.bytes().length,
        profileRequest.contentType()
    );
  }

}
