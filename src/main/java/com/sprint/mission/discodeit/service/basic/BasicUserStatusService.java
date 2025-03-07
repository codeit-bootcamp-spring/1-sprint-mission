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

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {

  private final UserService userService;
  private final UserStatusRepository userStatusRepository;

  @Override
  public UserStatus createUserStatus(CreateUserStatusRequest request) {
    User user = userService.getUserById(request.userId());
    if (userStatusRepository.existsByUser(user)) {
      throw new NoSuchElementException("Userstatus already exists");
    }
    return new UserStatus(user, request.lastActiveTime());
  }

  @Override
  public UserStatus getUserStatus(UUID userId) {
    User user = userService.getUserById(userId);
    return userStatusRepository.findByUser(user).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public UserStatus update(UUID userStatusId, UpdateUserStatusRequest request) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new NoSuchElementException("UserStatus not found"));
    userStatus.updateLastActiveAt(request.lastActiveAt());
    return userStatusRepository.save(userStatus);
  }

  @Override
  public void delete(UUID userStatusId) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new NoSuchElementException("UserStatus not found"));
    userStatusRepository.deleteById(userStatusId);
  }
}
