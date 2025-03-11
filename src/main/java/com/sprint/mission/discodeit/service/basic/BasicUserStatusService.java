package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.status.CreateUserStatusRequest;
import com.sprint.mission.discodeit.dto.status.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
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
  public UserStatus createUserStatus(CreateUserStatusRequest request) {
    User user = userService.getUserById(request.userId());
    if (userStatusRepository.existsByUser(user)) {
      throw new NoSuchElementException("Userstatus already exists");
    }
    UserStatus status = new UserStatus(request.lastActiveTime());
    status.setUser(user);

    return userStatusRepository.save(status);
  }

  @Override
  @Transactional(readOnly = true)
  public UserStatus getUserStatus(UUID userId) {
    User user = userService.getUserById(userId);
    return userStatusRepository.findByUser(user).orElseThrow(NoSuchElementException::new);
  }

  @Override
  @Transactional
  public UserStatus update(UUID userId, UpdateUserStatusRequest request) {
    User user = userService.getUserById(userId);
    UserStatus userStatus = userStatusRepository.findByUser(user)
        .orElseThrow(() -> new NoSuchElementException("UserStatus not found"));
    userStatus.updateLastActiveAt(request.lastActiveAt());
    return userStatusRepository.save(userStatus);
  }

  @Override
  @Transactional
  public void delete(UUID userStatusId) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new NoSuchElementException("UserStatus not found"));
    userStatusRepository.deleteById(userStatusId);
  }
}
