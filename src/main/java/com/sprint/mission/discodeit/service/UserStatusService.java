package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserStatusRequest;
import com.sprint.mission.discodeit.dto.UserStatusResponse;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  UserStatusResponse create(UUID userId);

  UserStatusResponse findById(UUID id);

  UserStatusResponse findByUserId(UUID userId);

  UserStatusResponse updateByUserId(UUID userId, UserStatusRequest.Update request);

  List<UserStatusResponse> findAll();

  void deleteById(UUID id);

  void deleteByUserId(UUID userId);
}
