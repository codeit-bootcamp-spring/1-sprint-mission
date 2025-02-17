package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateDTO;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.UserStatusType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {

    UUID create(UserStatusCreateDTO userStatusCreateDTO);
    UserStatus find(UUID id);
    List<UserStatus> findAll();
    UserStatus update(UserStatusUpdateDTO userStatusUpdateDTO);
    UserStatus updateByUserId(UUID userId,  Instant time);
    UUID delete(UUID id);
}
