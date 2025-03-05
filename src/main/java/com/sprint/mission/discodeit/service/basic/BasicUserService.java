package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binary.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.Interface.BinaryContentService;
import com.sprint.mission.discodeit.service.Interface.UserService;
import com.sprint.mission.discodeit.service.Interface.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentService binaryContentService;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusService userStatusService;
  private final UserStatusRepository userStatusRepository;


  @Override
  public User createUser(UserCreateRequestDto request,
      Optional<BinaryContentCreateRequestDto> profile) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new IllegalArgumentException("Email already in use");
    }
    if (userRepository.existsByName(request.getUsername())) {
      throw new IllegalArgumentException("Name already in use");
    }

    UUID profileId = profile
        .map(profileRequest -> {
          String fileName = profileRequest.getFileName();
          String contentType = profileRequest.getContentType();
          byte[] bytes = profileRequest.getBytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType, bytes);
          return binaryContentRepository.save(binaryContent).getId();
        })
        .orElse(null);

    User user = new User(request.getUsername(), request.getEmail(), request.getPassword(),
        profileId);
    User createUser = userRepository.save(user);

    Instant now = Instant.now();
    UserStatus userStatus = new UserStatus(createUser.getId(), now);
    userStatusRepository.save(userStatus);

    return createUser;
  }

  @Override
  public UserDto getUserById(UUID id) {
    return userRepository.getUserById(id)
        .map(this::toDto)
        .orElseThrow(() -> new IllegalArgumentException("User id : " + id + " not found"));
  }

  @Override
  public List<UserDto> getAllUsers() {
    return userRepository.getAllUsers().stream().map(this::toDto).toList();
  }

  @Override
  public List<User> findAllUsers() {
    return userRepository.getAllUsers();
  }


  @Override
  public User updateUser(UUID userId, UserUpdateRequestDto request
      , Optional<BinaryContentCreateRequestDto> profile) {
    User user = userRepository.getUserById(userId)
        .orElseThrow(() -> new NoSuchElementException("User not found"));

    if (userRepository.existsByEmail(request.getNewEmail())) {
      throw new IllegalArgumentException(
          "User with email " + request.getNewEmail() + " already exists");
    }
    if (userRepository.existsByName(request.getNewUsername())) {
      throw new IllegalArgumentException(
          "User with username " + request.getNewUsername() + " already exists");
    }
    UUID nullableProfileId = profile
        .map(profileRequest -> {
          Optional.ofNullable(user.getProfileId())
              .ifPresent(binaryContentRepository::deleteById);

          String fileName = profileRequest.getFileName();
          String contentType = profileRequest.getContentType();
          byte[] bytes = profileRequest.getBytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType, bytes);
          return binaryContentRepository.save(binaryContent).getId();
        })
        .orElse(null);

    Instant now = Instant.now();
    user.update(request.getNewUsername(), request.getNewEmail(), request.getNewPassword(),
        nullableProfileId);
    User updateUser = userRepository.save(user);
    UserStatusUpdateRequest statusUpdateRequest = new UserStatusUpdateRequest(now);
    userStatusService.updateByUserId(userId, statusUpdateRequest);

    return updateUser;
  }

  @Override
  public void deleteUser(UUID id) {
    if (!userRepository.existsById(id)) {
      throw new NoSuchElementException("User with id not found");
    }
    userRepository.deleteUser(id);
    userStatusService.deleteByUserId(id);
    binaryContentService.deleteByUserId(id);

  }

  private UserDto toDto(User user) {
    Boolean online = userStatusRepository.findByUserId(user.getId())
        .map(UserStatus::isOnline)
        .orElse(null);

    return new UserDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getProfileId(),
        online
    );
  }
}
