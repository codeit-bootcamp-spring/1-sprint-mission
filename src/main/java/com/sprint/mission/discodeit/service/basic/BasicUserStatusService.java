package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user_status.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.user_status.UserStatusDto;
import com.sprint.mission.discodeit.dto.user_status.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.user_status.DuplicateUserStatusException;
import com.sprint.mission.discodeit.exception.user_status.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> UserNotFoundException.withId(request.userId()));

    if (userStatusRepository.findByUserId(user.getId()).isPresent()) {
      throw DuplicateUserStatusException.withUserId(request.userId());
    }

    UserStatus userStatus = new UserStatus(user, request.lastActiveAt());
    userStatusRepository.save(userStatus);
    log.info("사용자 상태 생성 완료: id={}, userId={}", userStatus.getId(), user.getId());
    return userStatusMapper.toDto(userStatus);
  }


  @Transactional
  @Override
  public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    log.debug("사용자 ID로 상태 수정 시작: userId={}, newLastActiveAt={}",
        userId, request.newLastActiveAt());
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> UserStatusNotFoundException.withUserId(userId));

    userStatus.update(request.newLastActiveAt());
    log.info("사용자 ID로 상태 수정 완료: userId={}", userId);
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional(readOnly = true)
  @Override
  public UserStatusDto find(UUID userStatusId) {
    log.debug("사용자 상태 조회 시작: id={}", userStatusId);
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> UserStatusNotFoundException.withId(userStatusId));
    log.info("사용자 상태 조회 완료: id={}", userStatusId);
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public void delete(UUID userStatusId) {
    log.debug("사용자 상태 삭제 시작: id={}", userStatusId);
    userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> UserStatusNotFoundException.withId(userStatusId));
    log.info("사용자 상태 삭제 완료: id={}", userStatusId);
    userStatusRepository.deleteById(userStatusId);
  }
}
