package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
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
    if (!userRepository.existsById(request.userId())) {
      throw new NoSuchElementException("아이디가 " + request.userId() + "인 회원이 존재하지 않습니다.");
    }
    List<UserStatus> userStatusList = userStatusRepository.findAllByUserId(request.userId());
    for (UserStatus status : userStatusList) {
      if (status.getUser().getId().equals(request.userId())) {
        throw new IllegalArgumentException("해당 User Status가 이미 존재합니다.");
      }
    }

    UUID userId = request.userId();
    User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

    Instant lastActiveAt = request.lastActiveAt();
    UserStatus userStatus = UserStatus.builder()
        .user(user)
        .lastActiveAt(lastActiveAt)
        .build();
    return toDto(userStatusRepository.save(userStatus));
  }

  @Override
  public UserStatus find(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("아이디가" + userStatusId + "인 회원 상태가 존재하지 않습니다."));
  }

  @Override
  public List<UserStatus> findAllByUserId(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new NoSuchElementException("아이디가" + userId + "인 회원이 존재하지 않습니다.");
    }
    return userStatusRepository.findAllByUserId(userId);
  }


  @Transactional
  @Override
  public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request) {
    Instant lastActiveAt = request.newLastActiveAt();
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("아이디가 " + userStatusId + "인 회원 상태가 존재하지 않습니다."));
    userStatus.update(lastActiveAt);
    return toDto(userStatus);
  }

  @Transactional
  @Override
  public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    if (!userRepository.existsById(userId)) {
      throw new NoSuchElementException("아이디가" + userId + "인 회원이 존재하지 않습니다.");
    }
    Instant lastActiveAt = request.newLastActiveAt();
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new NoSuchElementException("회원 상태가 존재하지 않습니다."));

    userStatus.update(lastActiveAt);
    return toDto(userStatus);
  }


  @Transactional
  @Override
  public void delete(UUID userStatusId) {
    if (!userRepository.existsById(userStatusId)) {
      throw new NoSuchElementException("아이디가" + userStatusId + "인 회원 상태가 존재하지 않습니다.");
    }
    userStatusRepository.deleteById(userStatusId);
  }
}

