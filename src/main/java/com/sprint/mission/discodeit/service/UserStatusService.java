package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.status.CreateUserStatusRequest;
import com.sprint.mission.discodeit.dto.status.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.UUID;

public interface UserStatusService {

  UserStatus createUserStatus(CreateUserStatusRequest request);

  UserStatus getUserStatus(UUID userId);

  UserStatus update(UUID userStatusId, UpdateUserStatusRequest request);

  void delete(UUID userStatusId);
}
