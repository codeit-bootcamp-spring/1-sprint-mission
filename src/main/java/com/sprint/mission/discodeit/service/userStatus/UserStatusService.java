package com.sprint.mission.discodeit.service.userStatus;

import com.sprint.mission.discodeit.dto.userStatusService.UserStatusCreateRequestDTO;
import com.sprint.mission.discodeit.dto.userStatusService.UserStatusUpdateRequestDTO;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;
import java.util.List;

public interface UserStatusService {
    UserStatus create(UserStatusCreateRequestDTO request);
    UserStatus find(UUID ID);
    List<UserStatus> findAll();
    UserStatus update(UserStatusUpdateRequestDTO request);
    UserStatus updateByUserId(UUID userId);
    void delete(UUID id);
}
