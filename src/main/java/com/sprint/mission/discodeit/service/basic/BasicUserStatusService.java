package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.dto.user.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserStatusUpdateByUserIdRequest;
import com.sprint.mission.discodeit.dto.user.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

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
            () -> new NoSuchElementException("UserStatus(" + userStatusId + ")가 존재하지 않습니다."));
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public UserStatusDto findUserStatusByUserId(UUID userId) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(
            () -> new NoSuchElementException("userId(" + userId + ")인 UserStatus가 존재하지 않습니다."));
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
            .orElseThrow(() -> new NoSuchElementException(
                "UserStatus(" + request.UserStatusId() + ")가 없습니다."));

    userStatus.updateLastConnectAt(request.lastConnectTime());
    userStatus.refreshUpdateAt();
    // userStatusRepository.save(userStatus); JPA 의 더티 채킹으로 save 하지 않아도 DB에 자동 업데이트

    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public UserStatusDto updateUserStatusByUserId(UUID userId,
      UserStatusUpdateByUserIdRequest request) {
    UserStatus userStatus =
        userStatusRepository.findByUserId(userId)
            .orElseThrow(
                () -> new NoSuchElementException("userId(" + userId + ")인 UserStatus가 존재하지 않습니다."));

    userStatus.updateLastConnectAt(request.newLastActiveAt());
    userStatus.refreshUpdateAt();
    // userStatusRepository.save(userStatus); JPA 의 더티 채킹으로 save 하지 않아도 DB에 자동 업데이트

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
