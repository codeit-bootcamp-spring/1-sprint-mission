package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.status.CreateUserStatusRequest;
import com.sprint.mission.discodeit.dto.status.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.dto.status.UserStatusResponse;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.UUID;

public interface UserStatusService {

  UserStatusResponse createUserStatus(CreateUserStatusRequest request);

  UserStatusResponse getUserStatus(UUID userId);

  UserStatusResponse update(UUID userStatusId, UpdateUserStatusRequest request);

  void delete(UUID userStatusId);
}
