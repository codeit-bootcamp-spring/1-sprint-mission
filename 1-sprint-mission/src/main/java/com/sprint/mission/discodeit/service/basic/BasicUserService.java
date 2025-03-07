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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
  private final UserStatusRepository userStatusRepository;


  @Transactional
  @Override
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

    BinaryContent profile = optionalProfileCreateRequest
        .map(profileRequest -> new BinaryContent(
            profileRequest.fileName(),
            (long) profileRequest.bytes().length,
            profileRequest.contentType(),
            profileRequest.bytes()
        ))
        .orElse(null);

    User user = new User(username, email, userCreateRequest.password(), profile);

    user.setStatus(new UserStatus(user, Instant.now()));
    return userRepository.save(user);
  }

  @Override
  public UserDto find(UUID userId) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new NoSuchElementException("User with id " + userId + " not found"));
    return toDto(user);
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAll()
        .stream()
        .map(this::toDto)
        .toList();
  }

  @Transactional
  @Override
  public User update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

    String newUsername = userUpdateRequest.newUsername();
    String newEmail = userUpdateRequest.newEmail();
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

    user.update(newUsername, newEmail, userUpdateRequest.newPassword(), user.getProfile());

    return user;
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

  private UserDto toDto(User user) {
    return new UserDto(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        user.getProfile() != null ? user.getProfile().getId() : null,
        user.getStatus() != null && user.getStatus().isOnline()
    );
  }
}
