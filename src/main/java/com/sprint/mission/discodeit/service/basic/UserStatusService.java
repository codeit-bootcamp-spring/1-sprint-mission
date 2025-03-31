package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Transactional
  public UserStatusDto create(UserStatusCreateRequest request) {
    UUID userId = request.userId();

    User user = userRepository.findById(userId)
        .orElseGet(() -> {
          log.warn("사용자를 찾을 수 없음 - userId: {}", userId);
          throw new NoSuchElementException("User with id " + userId + " not found");
        });
    Optional.ofNullable(user.getStatus())
        .ifPresent(status -> {
          log.warn("사용자 상태가 이미 존재함 - userId: {}", userId);
          throw new IllegalArgumentException("UserStatus with id " + userId + " already exists");
        });

    Instant lastActiveAt = request.lastActiveAt();
    UserStatus userStatus = new UserStatus(user, lastActiveAt);
    userStatusRepository.save(userStatus);
    log.info("사용자 상태 저장 완료 - userId: {}", userId);
    return userStatusMapper.toDto(userStatus);
  }

  public UserStatusDto find(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId)
        .map(userStatusMapper::toDto)
        .orElseThrow(
            () -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found"));
  }

  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll().stream()
        .map(userStatusMapper::toDto)
        .toList();
  }

  @Transactional
  public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();
    log.debug("사용자 상태 업데이트 서비스 진입 - userStatusId: {}, newLastActiveAt: {}", userStatusId,
        newLastActiveAt);
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseGet(() -> {
          log.warn("사용자 상태를 찾을 수 없음 - userStatusId: {}", userStatusId);
          throw new NoSuchElementException("UserStatus with id " + userStatusId + " not found");
        });
    userStatus.update(newLastActiveAt);
    log.info("사용자 상태 업데이트 완료 - userStatusId: {}", userStatusId);
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();
    log.debug("사용자 상태 업데이트 서비스 진입 - userId: {}, newLastActiveAt: {}", userId,
        newLastActiveAt);

    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseGet(
            () -> {
              log.warn("사용자 상태를 찾을 수 없음 - userId: {}", userId);
              throw new NoSuchElementException("UserStatus with userId " + userId + " not found");
            });
    userStatus.update(newLastActiveAt);
    log.info("사용자 상태 업데이트 완료 - userId: {}", userId);
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  public void delete(UUID userStatusId) {
    if (!userStatusRepository.existsById(userStatusId)) {
      log.warn("사용자 상태를 찾을 수 없음 - userStatusId: {}", userStatusId);
      throw new NoSuchElementException("UserStatus with id " + userStatusId + " not found");
    }
    userStatusRepository.deleteById(userStatusId);
    log.info("사용자 상태 삭제 완료 - userStatusId: {}", userStatusId);
  }
}
