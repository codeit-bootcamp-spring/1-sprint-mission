package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;

  @Override
  public UserStatus create(UserStatusCreateRequest request) {
    UUID userId = request.userId();

    if (!userRepository.existsById(userId)) {
      throw new NoSuchElementException("User with id " + userId + " does not exist");
    }
    if (userStatusRepository.findByUserId(userId).isPresent()) {
      throw new IllegalArgumentException("UserStatus with id " + userId + " already exists");
    }

    Instant lastActiveAt = request.lastActiveAt();
    UserStatus userStatus = new UserStatus(userId, lastActiveAt);
    return userStatusRepository.save(userStatus);
  }

  @Override
  public UserStatus find(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found"));
  }

  @Override
  public List<UserStatus> findAll() {
    return userStatusRepository.findAll().stream()
        .toList();
  }

  @Override
  public UserStatus update(UUID userStatusId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();

    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found"));
    userStatus.update(newLastActiveAt);

    return userStatusRepository.save(userStatus);
  }

  @Override
  public UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();

    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(
            () -> new NoSuchElementException("UserStatus with userId " + userId + " not found"));
    userStatus.update(newLastActiveAt);

    return userStatusRepository.save(userStatus);
  }

  @Override
  public void delete(UUID userStatusId) {
    if (!userStatusRepository.existsById(userStatusId)) {
      throw new NoSuchElementException("UserStatus with id " + userStatusId + " not found");
    }
    userStatusRepository.deleteById(userStatusId);
  }
}
