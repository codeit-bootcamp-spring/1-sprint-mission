package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusAlreadyExistException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class BasicUserStatusService extends UserStatusMapper implements UserStatusService {

  @Autowired
  UserStatusRepository userStatusRepository;
  UserRepository userRepository;

  @Transactional
  @Override
  public UserStatusDto create(UserStatusCreateRequest request) {
    // 파라미터 예외 처리 -> 레포지토리.save (변수에 dto 파라미터 할당, 객체 생성, 레포지토리.save(객체 전달))
    if (!userRepository.existsById(request.getUserId())) {
      throw new UserNotFoundException(null);
    }
    List<UserStatus> userStatusList = userStatusRepository.findAllByUserId(request.getUserId());
    for (UserStatus status : userStatusList) {
      if (status.getUser().getId().equals(request.getUserId())) {
        throw new UserStatusAlreadyExistException(null);
      }
    }

    UUID userId = request.getUserId();
    User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(null));

    Instant lastActiveAt = request.getLastActiveAt();
    UserStatus userStatus = UserStatus.builder()
        .user(user)
        .lastActiveAt(lastActiveAt)
        .build();
    return toDto(userStatusRepository.save(userStatus));
  }

  @Transactional(readOnly = true)
  @Override
  public UserStatus find(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> new UserNotFoundException(null));
  }

  @Transactional(readOnly = true)
  @Override
  public List<UserStatus> findAllByUserId(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new UserNotFoundException(null);
    }
    return userStatusRepository.findAllByUserId(userId);
  }


  @Transactional
  @Override
  public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request) {
    Instant lastActiveAt = request.getNewLastActiveAt();
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> new UserStatusNotFoundException(null));
    userStatus.update(lastActiveAt);
    return toDto(userStatus);
  }

  @Transactional
  @Override
  public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    if (!userRepository.existsById(userId)) {
      throw new UserNotFoundException(null);
    }
    Instant lastActiveAt = request.getNewLastActiveAt();
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new UserStatusNotFoundException(null));

    userStatus.update(lastActiveAt);
    return toDto(userStatus);
  }


  @Transactional
  @Override
  public void delete(UUID userStatusId) {
    if (!userRepository.existsById(userStatusId)) {
      throw new UserStatusNotFoundException(null);
    }
    userStatusRepository.deleteById(userStatusId);
  }
}

