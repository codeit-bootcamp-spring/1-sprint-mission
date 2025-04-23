package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateByUserIdRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.transaction.Transactional;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j // 로깅을 위한 Lombok 어노테이션 추가
@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  //
  private final UserStatusMapper userStatusMapper;

  @Transactional
  @Override
  public UserStatusDto createUserStatus(UserStatusCreateRequest request) {
    UserStatus userStatus = UserStatus.builder()
        .user(request.user())
        .lastActiveAt(request.lastConnectAt())
        .build();
    userStatusRepository.save(userStatus);
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public UserStatusDto findUserStatusById(UUID userStatusId) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> new UserStatusNotFoundException(Map.of("userStatusId", userStatusId)));
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public UserStatusDto findUserStatusByUserId(UUID userId) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(
            () -> new UserStatusNotFoundException(Map.of("userId", userId)));
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public List<UserStatusDto> findAllUserStatus() {
    // TODO 예외 처리
    return userStatusRepository.findAll().stream()
        .map(userStatusMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public UserStatusDto updateUserStatus(UserStatusUpdateRequest request) {
    UserStatus userStatus =
        userStatusRepository.findById(request.UserStatusId())
            .orElseThrow(
                () -> new UserStatusNotFoundException(
                    Map.of("userStatusId", request.UserStatusId())));

    userStatus.updateLastConnectAt(request.lastConnectTime());
    userStatus.refreshUpdateAt();
    // userStatusRepository.save(userStatus); JPA 의 더티 채킹으로 save 하지 않아도 DB에 자동 업데이트

    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public UserStatusDto updateUserStatusByUserId(UUID userId,
      UserStatusUpdateByUserIdRequest request) {
    log.info("UserId로 유저 상태 업데이트 시도");
    UserStatus userStatus =
        userStatusRepository.findByUserId(userId)
            .orElseThrow(
                () -> new UserStatusNotFoundException(Map.of("userId", userId)));

    userStatus.updateLastConnectAt(request.newLastActiveAt());
    userStatus.refreshUpdateAt();
    // userStatusRepository.save(userStatus); JPA 의 더티 채킹으로 save 하지 않아도 DB에 자동 업데이트
    log.info("UserId로 유저 상태 업데이트 시도 성공");
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public void deleteUserStatusById(UUID userStatusId) {
    userStatusRepository.deleteById(userStatusId);
  }

  @Transactional
  @Override
  public void delteUserStatusByUserId(UUID userId) {
    userStatusRepository.deleteByUserId(userId);
  }
}
