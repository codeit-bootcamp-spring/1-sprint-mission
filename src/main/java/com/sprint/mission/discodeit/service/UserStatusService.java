package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Dto.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.HashMap;
import java.util.UUID;

public interface UserStatusService {
    UserStatus createUserStatus(UUID userId);
    UserStatusDto findUserStatusByUserId(UUID userId);
    HashMap<UUID, UserStatusDto> findAllUserStatusByUserId();
    boolean updateUserStatus(UUID userId);
    boolean deleteUserStatus(UUID userId);
}
