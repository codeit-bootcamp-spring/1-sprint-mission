package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserStatusDto;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {
  UserStatusDto createUserStatus(UUID paramUUID, Instant paramInstant);
  UserStatusDto findUserStatus(UUID paramUUID);
  List<UserStatusDto> findAllUserStatus();
  UserStatusDto updateUserStatus(UUID paramUUID);
  void deleteUserStatus(UUID paramUUID);
}
