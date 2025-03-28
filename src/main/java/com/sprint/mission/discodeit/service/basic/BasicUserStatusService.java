package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user_status.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.user_status.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;

  @Transactional
  @Override
  public UserStatus create(UserStatusCreateRequest request) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new NoSuchElementException("유저가 존재하지 않습니다."));

    if (userStatusRepository.findByUserId(user.getId()).isPresent()) {
      throw new IllegalArgumentException("유저상태가 이미 존재합니다.");
    }

    UserStatus userStatus = new UserStatus(user, request.lastActiveAt());
    return userStatusRepository.save(userStatus);
  }


  @Transactional
  @Override
  public UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new NoSuchElementException("유저상태가 존재하지 않습니다."));

    userStatus.update(request.newLastActiveAt());

    return userStatus;
  }

  @Transactional(readOnly = true)
  @Override
  public UserStatus findByUserId(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new NoSuchElementException("유저상태가 존재하지 않습니다."));
  }

  @Transactional
  @Override
  public void delete(UUID userStatusId) {
    userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new NoSuchElementException("유저상태가 존재하지 않습니다."));

    userStatusRepository.deleteById(userStatusId);
  }
}
