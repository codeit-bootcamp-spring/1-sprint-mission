package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user_status.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.user_status.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;

public interface UserStatusService {

  UserStatus create(UserStatusCreateRequest request);

  UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest request);

  UserStatus findByUserId(UUID userStatusId);

  void delete(UUID userStatusId);
}
