package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;

  @Override
  public UserStatusDto create(UserStatusCreateRequest request) {
    UUID userId = request.userId();

    if (!userRepository.existsById(userId)) {
      throw new ResourceNotFoundException("User not found: " + userId);
    }
    if (userStatusRepository.findByUserId(userId).isPresent()) {
      throw new ResourceNotFoundException("User status not found: " + userId);
    }

    Instant lastActiveAt = request.lastActiveAt();
    UserStatus userStatus = new UserStatus(userId, lastActiveAt);
    return toDto(userStatusRepository.save(userStatus));
  }

  @Override
  public UserStatusDto findById(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId)
        .map(this::toDto)
        .orElseThrow(() -> new ResourceNotFoundException("User status not found: " + userStatusId));
  }

  @Override
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll().stream()
        .map(this::toDto)
        .toList();
  }

  @Override
  public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();

    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new ResourceNotFoundException("User status not found: " + userStatusId));
    userStatus.update(newLastActiveAt);

    return toDto(userStatusRepository.save(userStatus));
  }

  @Override
  public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();

    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User status not found: " + userId));
    userStatus.update(newLastActiveAt);

    return toDto(userStatusRepository.save(userStatus));
  }

  @Override
  public void delete(UUID userStatusId) {
    if (!userStatusRepository.existsById(userStatusId)) {
      throw new ResourceNotFoundException("User status not found: " + userStatusId);
    }
    userStatusRepository.deleteById(userStatusId);
  }

  private UserStatusDto toDto(UserStatus userStatus) {
    return new UserStatusDto(userStatus.getId(),
        userStatus.getCreatedAt(),
        userStatus.getUpdatedAt(),
        userStatus.getUserId(),
        userStatus.getLastActiveAt(),
        userStatus.getOnline());
  }
}
