package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Override
  public UserStatusDto create(UserStatusCreateRequest request) {

    log.debug("사용자 상태 생성 시작: userId={}", request.userId());

    UUID userId = request.userId();
    User user = userRepository.findById(userId)
        .orElseThrow(() -> UserNotFoundException.withId(userId));
    Optional.ofNullable(user.getStatus())
        .ifPresent(status -> {
          throw UserNotFoundException.withId(userId);
        });

    Instant lastActiveAt = request.lastActiveAt();
    UserStatus userStatus = new UserStatus(user, lastActiveAt);
    userStatusRepository.save(userStatus);

    log.info("사용자 상태 생성 완료: id={}, userId={}", userStatus.getId(), userId);

    return userStatusMapper.toDto(userStatus);
  }

  @Override
  @Transactional(readOnly = true)
  public UserStatusDto find(UUID userStatusId) {

    log.debug("사용자 상태 조회 시작: id={}", userStatusId);

    UserStatusDto userStatusDto = userStatusRepository.findById(userStatusId)
        .map(userStatusMapper::toDto)
        .orElseThrow(() -> UserStatusNotFoundException.withId(userStatusId));

    log.info("사용자 상태 조회 완료: id={}", userStatusId);

    return userStatusDto;
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserStatusDto> findAll() {

    log.debug("전체 사용자 상태 목록 조회 시작");

    List<UserStatusDto> userStatusDtos = userStatusRepository.findAll().stream()
        .map(userStatusMapper::toDto)
        .toList();

    log.info("전체 사용자 상태 목록 조회 완료: count={}", userStatusDtos.size());

    return userStatusDtos;
  }

  @Override
  public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request) {

    Instant newLastActiveAt = request.newLastActiveAt();

    log.debug("사용자 상태 수정 시작: id={}, newLastActiveAt={}",
        userStatusId, newLastActiveAt);

    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> UserStatusNotFoundException.withId(userStatusId));
    userStatus.update(newLastActiveAt);

    log.info("사용자 상태 수정 완료: id={}", userStatusId);

    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {

    Instant newLastActiveAt = request.newLastActiveAt();

    log.debug("사용자 ID로 상태 수정 시작: userId={}, newLastActiveAt={}",
        userId, newLastActiveAt);

    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> UserStatusNotFoundException.withUserId(userId));
    userStatus.update(newLastActiveAt);

    log.info("사용자 ID로 상태 수정 완료: userId={}", userId);

    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public void delete(UUID userStatusId) {

    log.debug("사용자 상태 삭제 시작: id={}", userStatusId);

    if (!userStatusRepository.existsById(userStatusId)) {
      throw UserStatusNotFoundException.withId(userStatusId);
    }
    userStatusRepository.deleteById(userStatusId);

    log.info("사용자 상태 삭제 완료: id={}", userStatusId);
  }
}
