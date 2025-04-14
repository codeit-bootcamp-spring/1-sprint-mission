package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatusDto.CreateUserStatusRequest;
import com.sprint.mission.discodeit.dto.userStatusDto.UpdateUserStatusRequest;
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
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Override
  @Transactional
  public UserStatusDto create(CreateUserStatusRequest request) {

    log.debug("UserStatus 생성 시작: userId={}", request.userId());

    UUID userId = request.userId();

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
    Optional.ofNullable(user.getStatus())
        .ifPresent(status -> {
          throw new UserNotFoundException(userId);
        });

    Instant lastActiveAt = request.lastActiveAt();
    UserStatus userStatus = new UserStatus(user, lastActiveAt);
    userStatusRepository.save(userStatus);

    log.info("UserStatus 생성 완료: id={}, userId={}", userStatus.getId(), userId);

    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public UserStatusDto find(UUID userStatusId) {

    log.debug("UserStatus 조회 시작: id={}", userStatusId);

    UserStatusDto userStatusDto = userStatusRepository.findById(userStatusId)
        .map(userStatusMapper::toDto)
        .orElseThrow(() -> new UserStatusNotFoundException(userStatusId));

    log.info("UserStatus 조회 완료: id={}", userStatusId);

    return userStatusDto;
  }

  @Override
  public List<UserStatusDto> findAll() {

    log.debug("전체 UserStatus 목록 조회 시작");

    List<UserStatusDto> userStatusDtos = userStatusRepository.findAll().stream()
        .map(userStatusMapper::toDto)
        .toList();

    log.info("전체 UserStatus 목록 조회 완료: 조회된 항목 수={}", userStatusDtos.size());

    return userStatusDtos;
  }

  @Override
  @Transactional
  public UserStatusDto update(UUID userStatusId, UpdateUserStatusRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();

    log.debug("UserStatus 수정 시작: id={}, newLastActiveAt={}",
        userStatusId, newLastActiveAt);

    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new UserStatusNotFoundException(userStatusId));
    userStatus.update(newLastActiveAt);

    log.info("UserStatus 수정 완료: id={}", userStatusId);

    return userStatusMapper.toDto(userStatus);
  }

  @Override
  @Transactional
  public UserStatusDto updateByUserId(UUID userId, UpdateUserStatusRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();

    log.debug("사용자 ID로 UserStatus 수정 시작: userId={}, newLastActiveAt={}",
        userId, newLastActiveAt);

    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
    userStatus.update(newLastActiveAt);

    log.info("사용자 ID로 UserStatus 수정 완료: userId={}", userId);

    return userStatusMapper.toDto(userStatus);
  }

  @Override
  @Transactional
  public void delete(UUID userStatusId) {

    log.debug("UserStatus 삭제 시작: id={}", userStatusId);

    if (!userStatusRepository.existsById(userStatusId)) {
      throw new UserStatusNotFoundException(userStatusId);
    }
    userStatusRepository.deleteById(userStatusId);
    log.info("UserStatus 삭제 완료: id={}", userStatusId);
  }
}
