package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserStatusDTO;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UserStatusDTO.CreateUserStatusDTO createUserStatusDTO);
    UserStatus find(UUID id);
    List<UserStatus> findAll();
    UserStatus update(UUID id, UserStatusDTO.UpdateUserStatusDTO updateUserStatusDTO);
    UserStatus updateByUserId(UUID userId, UserStatusDTO.UpdateUserStatusDTO updateUserStatusDTO);
    void delete(UUID id);
}
