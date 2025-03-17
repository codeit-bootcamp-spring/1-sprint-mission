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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Slf4j
public class BasicUserService implements UserService {

  private final UserRepository userRepository;

  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserMapper userMapper;

  private final BinaryContentStorage binaryContentStorage;

  @Override
  @Transactional
  public User create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    String username = userCreateRequest.username();
    String email = userCreateRequest.email();

    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("User with email " + email + " already exists");
    }
    if (userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("User with username " + username + " already exists");
    }

    User user = new User(userCreateRequest.username(),
        userCreateRequest.email(),
        userCreateRequest.password());

    if (optionalProfileCreateRequest.isPresent()) {
      BinaryContentCreateRequest profileRequest = optionalProfileCreateRequest.get();
      String fileName = profileRequest.fileName();
      String contentType = profileRequest.contentType();
      byte[] bytes = profileRequest.bytes();

      BinaryContent profile = new BinaryContent(fileName, (long) bytes.length, contentType);
      profile = binaryContentRepository.save(profile);

      binaryContentStorage.put(profile.getId(), bytes);

      user.updateProfile(profile);
    }
    User savedUser = userRepository.save(user);

    UserStatus userStatus = new UserStatus(savedUser);
    userStatusRepository.save(userStatus);

    return savedUser;
  }

  @Override
  @Transactional(readOnly = true)
  public UserDto find(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    return userMapper.toDto(user);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserDto> findAll() {
    List<User> users = userRepository.findAll();

    return userMapper.toDtoList(users);
  }


  @Override
  @Transactional
  public User update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

    if (user.getProfile() != null) {
      user.getProfile().getFileName();
    }

    String newUsername = userUpdateRequest.newUsername();
    String newEmail = userUpdateRequest.newEmail();
    if (newUsername != null && !newUsername.equals(user.getUsername()) &&
        userRepository.existsByUsername(newUsername)) {
      throw new IllegalArgumentException("User with username " + newUsername + " already exists");
    }
    if (newEmail != null && !newEmail.equals(user.getEmail()) &&
        userRepository.existsByEmail(newEmail)) {
      throw new IllegalArgumentException("User with email " + newEmail + " already exists");
    }

    if (optionalProfileCreateRequest.isPresent()) {
      BinaryContentCreateRequest profileRequest = optionalProfileCreateRequest.get();

      if (user.getProfile() != null) {
        UUID oldProfileId = user.getProfile().getId();
        try {

          user.updateProfile(null);
          userRepository.save(user);

          if (binaryContentRepository.existsById(oldProfileId)) {
            binaryContentRepository.deleteById(oldProfileId);
          }
        } catch (Exception e) {
          log.warn("Could not delete old profile with id {}: {}", oldProfileId, e.getMessage());
        }
      }

      String fileName = profileRequest.fileName();
      String contentType = profileRequest.contentType();
      byte[] bytes = profileRequest.bytes();
      BinaryContent newProfile = new BinaryContent(fileName, (long) bytes.length, contentType);
      newProfile = binaryContentRepository.save(newProfile);
      binaryContentStorage.put(newProfile.getId(), bytes);

      user.updateProfile(newProfile);
    }
    user.update(userUpdateRequest.newUsername(), userUpdateRequest.newEmail(),
        userUpdateRequest.newPassword());
    return userRepository.save(user);

  }

  @Override
  @Transactional
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

    if (user.getProfile() != null) {
      BinaryContent profile = user.getProfile();
      user.update(null, null, null);
      binaryContentRepository.delete(profile);
    }

    userRepository.delete(user);
  }

}
