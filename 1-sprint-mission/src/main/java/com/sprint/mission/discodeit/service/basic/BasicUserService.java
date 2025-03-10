package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
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

  @Transactional
  @Override
  public UserDto create(UserCreateRequest request,
      Optional<BinaryContentCreateRequest> optionalProfile) {
    if (userRepository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("User with email " + request.email() + " already exists");
    }
    if (userRepository.existsByUsername(request.username())) {
      throw new IllegalArgumentException(
          "User with username " + request.username() + " already exists");
    }
    BinaryContent profile = optionalProfile
        .map(profileRequest -> new BinaryContent(
            profileRequest.fileName(),
            (long) profileRequest.bytes().length,
            profileRequest.contentType(),
            profileRequest.bytes()
        ))
        .orElse(null);

    User user = new User(request.username(), request.email(), request.password(), profile);
    user = userRepository.save(user);
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
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

    String newUsername = request.newUsername();
    String newEmail = request.newEmail();
    if (userRepository.existsByEmail(newEmail) && !user.getEmail().equals(newEmail)) {
      throw new IllegalArgumentException("User with email " + newEmail + " already exists");
    }
    if (userRepository.existsByUsername(newUsername) && !user.getUsername().equals(newUsername)) {
      throw new IllegalArgumentException("User with username " + newUsername + " already exists");
    }

    optionalProfileCreateRequest
        .ifPresent(
            profileRequest -> {
              if (user.getProfile() != null) {
                binaryContentRepository.delete(user.getProfile());
              }
              BinaryContent newProfile = new BinaryContent(
                  profileRequest.fileName(),
                  (long) profileRequest.bytes().length,
                  profileRequest.contentType(),
                  profileRequest.bytes()
              );
              user.setProfile(newProfile);
            });

    user.update(newUsername, newEmail, request.newPassword(), user.getProfile());
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

}
