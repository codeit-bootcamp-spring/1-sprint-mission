package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.status.CreateUserStatusRequest;
import com.sprint.mission.discodeit.dto.status.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.dto.status.UserStatusResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {

  private final UserService userService;
  private final UserStatusRepository userStatusRepository;

  @Override
  @Transactional
  public UserStatusResponse createUserStatus(CreateUserStatusRequest request) {
    User user = userService.getUserById(request.userId());
    if (userStatusRepository.existsByUser(user)) {
      throw new NoSuchElementException("Userstatus already exists");
    }
    UserStatus status = new UserStatus(request.lastActiveTime());
    status.setUser(user);
    UserStatus savedStatus = userStatusRepository.save(status);

    return UserStatusMapper.INSTANCE.userStatusToUserStatusResponse(savedStatus);
  }

  @Override
  @Transactional(readOnly = true)
  public UserStatusResponse getUserStatus(UUID userId) {
    User user = userService.getUserById(userId);
    UserStatus userStatus = userStatusRepository.findByUser(user)
        .orElseThrow(NoSuchElementException::new);
    return UserStatusMapper.INSTANCE.userStatusToUserStatusResponse(userStatus);
  }

  @Override
  @Transactional
  public UserStatusResponse update(UUID userId, UpdateUserStatusRequest request) {
    User user = userService.getUserById(userId);
    UserStatus userStatus = userStatusRepository.findByUser(user)
        .orElseThrow(() -> new NoSuchElementException("UserStatus not found"));
    userStatus.updateLastActiveAt(request.lastActiveAt());
    UserStatus savedStatus = userStatusRepository.save(userStatus);
    return UserStatusMapper.INSTANCE.userStatusToUserStatusResponse(savedStatus);
  }

  @Override
  @Transactional
  public void delete(UUID userStatusId) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new NoSuchElementException("UserStatus not found"));
    userStatusRepository.deleteById(userStatusId);
  }
}
