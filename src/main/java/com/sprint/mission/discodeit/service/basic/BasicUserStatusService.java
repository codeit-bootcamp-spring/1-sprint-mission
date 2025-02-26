package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.validation.UserStatusValidator;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserStatusValidator userStatusValidator;
  private final UserStatusMapper userStatusMapper;

  @Override
  public UserStatusDto create(UserStatusCreateRequest request) {
    UUID userId = request.userId();
    Instant lastActiveAt = request.lastActiveAt();

    userStatusValidator.validateUser(userId);

    UserStatus userStatus = new UserStatus(userId, lastActiveAt);

    UserStatus createdUserStatus = userStatusRepository.save(userStatus);
    return userStatusMapper.userStatusEntityToDto(createdUserStatus);
  }

  @Override
  public UserStatusDto findById(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId)
        .map(userStatusMapper::userStatusEntityToDto)
        .orElseThrow(() -> new ResourceNotFoundException("User status not found: " + userStatusId));
  }

  @Override
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll().stream()
        .map(userStatusMapper::userStatusEntityToDto)
        .toList();
  }

  @Override
  public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new ResourceNotFoundException("User status not found: " + userStatusId));
    Instant newLastActiveAt = request.newLastActiveAt();
    userStatus.update(newLastActiveAt);
    UserStatus updatedUserStatus = userStatusRepository.save(userStatus);
    return userStatusMapper.userStatusEntityToDto(updatedUserStatus);
  }

  @Override
  public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(
            () -> new ResourceNotFoundException("User status not found: User id: " + userId));
    Instant newLastActiveAt = request.newLastActiveAt();
    userStatus.update(newLastActiveAt);
    UserStatus updatedUserStatus = userStatusRepository.save(userStatus);
    return userStatusMapper.userStatusEntityToDto(updatedUserStatus);
  }

  @Override
  public void delete(UUID userStatusId) {
    userStatusValidator.validateUserStatus(userStatusId);
    userStatusRepository.deleteById(userStatusId);
  }
}
