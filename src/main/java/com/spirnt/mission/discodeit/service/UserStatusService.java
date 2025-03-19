package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusDto;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  UserStatusDto create(UserStatusCreateRequest userStatusCreateRequest);

  UserStatusDto find(UUID userStatusId);

  List<UserStatusDto> findAll();

  UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest userStatusUpdateRequest,
      Instant serverTime);

  UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest);

  void delete(UUID userStatusId);


}
