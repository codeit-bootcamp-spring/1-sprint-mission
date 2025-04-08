package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userstatus.UserStatusAlreadyExistsException;
import com.sprint.mission.discodeit.exception.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
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
@Service
@Slf4j
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Transactional
  @Override
  public UserStatusDto create(UserStatusCreateRequest request) {
    UUID userId = request.userId();

    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.error("[유저 조회 실패] 해당 유저를 찾을 수 없습니다. 유저 ID: {}", userId);
          return new UserNotFoundException();
        });
    Optional.ofNullable(user.getStatus())
        .ifPresent(status -> {
          log.error("[유저 상태 정보 생성 실패] 해당 유저 상태 정보가 이미 존재합니다. 유저 ID: {}", userId);
          throw new UserStatusAlreadyExistsException();
        });

    Instant lastActiveAt = request.lastActiveAt();
    UserStatus userStatus = new UserStatus(user, lastActiveAt);
    log.info("[유저 상태 정보 생성 시도] 유저 상태 정보 ID: {}", userStatus.getId());

    userStatusRepository.save(userStatus);
    log.info("[유저 상태 정보 생성 성공] 유저 상태 정보 ID: {}", userStatus.getId());

    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public UserStatusDto find(UUID userStatusId) {
    log.info("[유저 상태 정보 조회 시도] 유저 상태 정보 ID: {}", userStatusId);

    return userStatusRepository.findById(userStatusId)
        .map(userStatusMapper::toDto)
        .orElseThrow(
            () -> {
              log.error("[유저 상태 조회 실패] 해당 유저 상태 정보를 찾을 수 없습니다. 유저 상태 정보 ID: {}", userStatusId);
              return new UserStatusNotFoundException();
            });
  }

  @Override
  public List<UserStatusDto> findAll() {
    log.info("[모든 유저 상태 정보 조회 시도]");

    return userStatusRepository.findAll().stream()
        .map(userStatusMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();
    log.info("[유저 상태 정보 수정 시도] 유저 상태 정보 ID: {}", userStatusId);

    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> {
              log.error("[유저 상태 정보 조회 실패] 해당 유저 상태 정보를 찾을 수 없습니다. 유저 상태 정보 ID: {}", userStatusId);
              return new UserStatusNotFoundException();
            });
    userStatus.update(newLastActiveAt);
    log.info("[유저 상태 정보 수정 성공] 유저 상태 정보 ID: {}", userStatusId);

    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();
    log.info("[유저 ID로 유저 상태 정보 수정 시도] 유저 ID: {}", userId);

    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(
            () -> {
              log.error("[유저 상태 정보 조회 실패] 해당 유저 ID의 유저 상태 정보를 찾을 수 없습니다. 유저 ID: {}", userId);
              return new UserStatusNotFoundException();
            });
    userStatus.update(newLastActiveAt);
    log.info("[유저 ID로 유저 상태 정보 수정 성공] 유저 ID: {}", userId);

    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public void delete(UUID userStatusId) {
    log.info("[유저 상태 정보 삭제 시도] 유저 상태 정보 ID: {}", userStatusId);

    if (!userStatusRepository.existsById(userStatusId)) {
      log.error("[유저 상태 정보 조회 실패] 해당 유저 상태 정보를 찾을 수 없습니다. 유저 상태 정보 ID: {}", userStatusId);
      throw new UserStatusNotFoundException();
    }
    userStatusRepository.deleteById(userStatusId);
    log.info("[유저 상태 정보 삭제 성공] 유저 상태 정보 ID: {}", userStatusId);

  }
}
