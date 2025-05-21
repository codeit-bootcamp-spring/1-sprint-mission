package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userstatus.DuplicateUserStatusException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Transactional
  @Override
  public UserStatusDto create(UserStatusCreateRequest request) {
    log.debug("사용자 상태 생성 시작: userId={}", request.userId());
    
    UUID userId = request.userId();
    User user = userRepository.findById(userId)
        .orElseThrow(() -> UserNotFoundException.withId(userId));
    
    Optional.ofNullable(user.getStatus())
        .ifPresent(status -> {
            throw DuplicateUserStatusException.withUserId(userId);
        });

    Instant lastActiveAt = request.lastActiveAt();
    UserStatus userStatus = new UserStatus(user, lastActiveAt);
    userStatusRepository.save(userStatus);
    
    log.info("사용자 상태 생성 완료: id={}, userId={}", userStatus.getId(), userId);
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public UserStatusDto find(UUID userStatusId) {
    log.debug("사용자 상태 조회 시작: id={}", userStatusId);
    UserStatusDto dto = userStatusRepository.findById(userStatusId)
        .map(userStatusMapper::toDto)
        .orElseThrow(() -> UserStatusNotFoundException.withId(userStatusId));
    log.info("사용자 상태 조회 완료: id={}", userStatusId);
    return dto;
  }

  @Override
  public List<UserStatusDto> findAll() {
    log.debug("전체 사용자 상태 목록 조회 시작");
    List<UserStatusDto> dtos = userStatusRepository.findAll().stream()
        .map(userStatusMapper::toDto)
        .toList();
    log.info("전체 사용자 상태 목록 조회 완료: 조회된 항목 수={}", dtos.size());
    return dtos;
  }

  @Transactional
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

  @Transactional
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

  @Transactional
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
