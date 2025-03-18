package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.user.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.user.userStatus.UserStatusUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  UserStatusDto create(UserStatusCreateRequest request);

  UserStatusDto find(UUID userStatusId);

  List<UserStatusDto> findAll();

  UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request);

  UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request);

  void delete(UUID userStatusId);
}
