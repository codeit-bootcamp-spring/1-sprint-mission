package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user_status.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.user_status.UserStatusDto;
import com.sprint.mission.discodeit.dto.user_status.UserStatusUpdateRequest;

import java.util.UUID;

public interface UserStatusService {

  UserStatusDto create(UserStatusCreateRequest request);

  UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request);

  UserStatusDto find(UUID userStatusId);

  void delete(UUID userStatusId);
}
