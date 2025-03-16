package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateDTO;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  UserStatusDto create(UserStatusCreateDTO userStatusCreateDTO);

  UserStatusDto find(UUID id);

  List<UserStatusDto> findAll();

  UserStatusDto update(UUID userId, UserStatusUpdateDTO userStatusUpdateDTO);

  UserStatusDto updateByUserId(UUID userId, Instant time);

  void delete(UUID id);
}
