package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public UserStatus create(UserStatusCreateRequest request) {
    UUID userId = request.userId();

    // User 객체를 먼저 가져옴
    var user = userRepository.findById(userId)
        .orElseThrow(
            () -> new NoSuchElementException("User with id " + userId + " does not exist"));

    // 중복 생성 방지 검증
    userStatusRepository.findByUserId(userId)
        .ifPresent(existingStatus -> {
          throw new IllegalArgumentException("UserStatus with id " + userId + " already exists");
        });

    // 엔티티 생성 및 저장
    UserStatus userStatus = new UserStatus(user, request.lastActiveAt());
    return userStatusRepository.save(userStatus);
  }

  @Override
  @Transactional
  public UserStatus find(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found"));
  }

  @Override
  @Transactional
  public List<UserStatus> findAll() {
    return userStatusRepository.findAll();
  }

  @Override
  @Transactional
  public UserStatus update(UUID userStatusId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();

    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found"));
    // 변경 감지로 인한 자동 업데이트
    userStatus.update(request.newLastActiveAt());
    return userStatusRepository.save(userStatus);
  }

  @Override
  @Transactional
  public UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();

    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(
            () -> new NoSuchElementException("UserStatus with userId " + userId + " not found"));
    // 변경 감지 및 저장
    userStatus.update(request.newLastActiveAt());
    return userStatusRepository.save(userStatus);
  }

  @Override
  @Transactional
  public void delete(UUID userStatusId) {
    if (!userStatusRepository.existsById(userStatusId)) {
      throw new NoSuchElementException("UserStatus with id " + userStatusId + " not found");
    }
    userStatusRepository.deleteById(userStatusId);
  }
}
